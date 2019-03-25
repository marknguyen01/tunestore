package com.tunestore.action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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

public class AddFriendAction extends Action implements IWithDataSource {
  private DataSource dataSource;
  
  private static final Log log = LogFactory.getLog(AddFriendAction.class);

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }
  
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    DynaActionForm daf = (DynaActionForm)form;
    ActionMessages messages = getMessages(request);
    ActionMessages errors = getErrors(request);
    
    Connection conn = null;
    
    try {
      conn = dataSource.getConnection();
      Statement stmt = conn.createStatement();
      
      ResultSet rs = stmt.executeQuery("SELECT COUNT(*) "
          + "FROM FRIENDSHIP "
          + "WHERE TUNEUSER1 = '"
          + daf.getString("friend")
          + "' AND TUNEUSER2 = '"
          + request.getSession(true).getAttribute("USERNAME")
          + "'");
      rs.next();
      // If they already tried to add me, we both approve theirs and
      // set this one pre-approved
      String sql;
      if (rs.getInt(1) > 0) {
        stmt.executeUpdate("UPDATE FRIENDSHIP SET APPROVED = 'Y' "
            + "WHERE TUNEUSER1 = '"
            + daf.getString("friend")
            + "' AND TUNEUSER2 = '"
            + request.getSession(true).getAttribute("USERNAME")
            + "'");
        sql = "INSERT INTO FRIENDSHIP VALUES ('"
          + request.getSession(true).getAttribute("USERNAME")
          + "','"
          + daf.getString("friend")
          + "','Y')";
      } else {
        sql = "INSERT INTO FRIENDSHIP VALUES ('"
          + request.getSession(true).getAttribute("USERNAME")
          + "','"
          + daf.getString("friend")
          + "','N')";
      }
      // We expect this part can fail if you add a friend you've already got
      try {
        stmt.executeUpdate(sql);
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("friend.added", daf.getString("friend")));
      } catch (Exception e) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("friend.duplicate"));
        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (conn != null) {
        try { conn.close(); } catch (Exception e) {}
      }
    }
    
    saveMessages(request, messages);
    saveErrors(request, errors);
    return mapping.findForward("success");
  }
}
