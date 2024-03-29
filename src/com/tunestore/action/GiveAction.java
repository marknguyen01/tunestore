package com.tunestore.action;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.tunestore.util.IWithDataSource;

public class GiveAction extends Action implements IWithDataSource {
  private DataSource dataSource;
  
  private static final Log log = LogFactory.getLog(GiveAction.class);

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }
  
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    ActionMessages errors = getErrors(request);
    ActionMessages messages = getMessages(request);
    DynaActionForm daf = (DynaActionForm)form;
    ActionForward retval = mapping.findForward("success");
    
    Connection conn = null;
    String sql = null;
    
    // Check if the token is valid
    String token = String.valueOf(request.getParameter("csrfToken"));
    String sessionToken = String.valueOf(request.getSession(true).getAttribute("TOKEN"));
    
    if(token != null && sessionToken != null && token.equals(sessionToken)) {
    	// check if user is logged in
    	if(request.getSession(true).getAttribute("USERNAME") != null) {
            try {
                conn = dataSource.getConnection();
                Statement stmt = conn.createStatement();
                
                // First, see if we have enough money
                sql = "SELECT TUNEUSER.BALANCE, "
                  + "CD.PRICE "
                  + "FROM TUNEUSER, CD "
                  + "WHERE TUNEUSER.USERNAME = '"
                  + request.getSession(true).getAttribute("USERNAME")
                  + "' AND CD.ID = "
                  + daf.getString("cd");
                log.info(sql);
                ResultSet rs = stmt.executeQuery(sql);
                boolean canbuy = true;
                if (rs.next()) {
                  canbuy = rs.getDouble(1) >= rs.getDouble(2);
                }
                rs.close();
                
                if (! canbuy) {
                  errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("give.nomoney"));
                } else {
                  // See if the recipient already has it
                  sql = "SELECT COUNT(*) rowcount "
                    + "FROM TUNEUSER_CD "
                    + "WHERE TUNEUSER_CD.TUNEUSER = '"
                    + daf.getString("friend")
                    + "' AND TUNEUSER_CD.CD = "
                    + daf.getString("cd");
                  log.info(sql);
                  rs = stmt.executeQuery(sql);
                  boolean friendhas = false;
                  if (rs.next()) {
                    friendhas = rs.getInt(1) >= 1;
                  }
                  rs.close();

                  // Deduct the amount of the CD from my account
                  sql = "UPDATE TUNEUSER SET BALANCE = BALANCE - (SELECT PRICE FROM CD WHERE CD.ID = "
                    + daf.getString("cd")
                    + ") WHERE TUNEUSER.USERNAME = '"
                    + request.getSession(true).getAttribute("USERNAME")
                    + "'";
                  log.info(sql);
                  stmt.executeUpdate(sql);

                  if (friendhas) {
                    // Friend has it, so we give them a gift of money
                    sql = "UPDATE TUNEUSER SET BALANCE = BALANCE + (SELECT PRICE FROM CD WHERE CD.ID = "
                      + daf.getString("cd")
                      + ") WHERE TUNEUSER.USERNAME = '"
                      + daf.getString("friend")
                      + "'";
                    log.info(sql);
                    stmt.executeUpdate(sql);
                    messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("give.already"));
                  } else {
                    sql = "INSERT INTO TUNEUSER_CD (TUNEUSER, CD) VALUES ('"
                      + daf.getString("friend")
                      + "', "
                      + daf.getString("cd")
                      + ")";
                    log.info(sql);
                    stmt.executeUpdate(sql);
                    messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("give.success"));
                  }
                }
              } catch (Exception e) {
                throw new RuntimeException(e);
              } finally {
                if (conn != null) {
                  try { conn.close(); } catch (Exception e) {}
                }
              }
            
            // Assign new token
            SecureRandom rand = new SecureRandom();
            request.getSession(true).setAttribute("TOKEN", rand.nextInt(1000));
            
    	}
    	else {
    		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("Please login in to perform this task!"));
    	}
        saveErrors(request, errors);
        saveMessages(request, messages);
        return retval;
    }
    else {
    	throw new ServletException("Invalid token!");
    }
  }
}
