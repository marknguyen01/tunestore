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

public class BuyAction extends Action implements IWithDataSource {
  private DataSource dataSource;
  
  private static final Log log = LogFactory.getLog(BuyAction.class);

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }
  
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    ActionForward retval = mapping.findForward("success");
    ActionMessages errors = getErrors(request);
    ActionMessages messages = getMessages(request);
    DynaActionForm daf = (DynaActionForm)form;
    
    Connection conn = null;
    String sql = null;

    if (request.getSession(true).getAttribute("USERNAME") == null) {
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("buy.login"));
      saveErrors(request, errors);
      return retval;
    }

    try {
      conn = dataSource.getConnection();
      Statement stmt = conn.createStatement();
      ResultSet rs = null;
      double price = 0.0;
      double balance = 0.0;

      // Get the price
      sql = "SELECT PRICE FROM CD WHERE ID = " + daf.getString("cd");
      log.info(sql);
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        price = rs.getDouble("PRICE");
      } else {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("buy.nocd"));
        saveErrors(request, errors);
        return retval;
      }
      rs.close();

      // Get my balance
      sql = "SELECT BALANCE FROM TUNEUSER WHERE USERNAME = '"
          + request.getSession(true).getAttribute("USERNAME") + "'";
      log.info(sql);
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        balance = rs.getDouble("BALANCE");
      }
      rs.close();

      if (balance <= price) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("buy.poor"));
        saveErrors(request, errors);
        return retval;
      } else {
        // Debit their account and buy the cd
        balance -= price;
        sql = "UPDATE TUNEUSER SET BALANCE = " + balance
          + " WHERE TUNEUSER.USERNAME = '"
          + request.getSession(true).getAttribute("USERNAME") + "'";
        log.info(sql);
        stmt.executeUpdate(sql);
        sql = "INSERT INTO TUNEUSER_CD VALUES ('"
          + request.getSession(true).getAttribute("USERNAME") + "',"
          + daf.getString("cd") + ")";
        log.info(sql);
        stmt.executeUpdate(sql);
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("buy.success"));
        saveMessages(request, messages);
        return retval;
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (conn != null) {
        try {
          conn.close();
        } catch (Exception e) {
        }
      }
    }
  }
}
