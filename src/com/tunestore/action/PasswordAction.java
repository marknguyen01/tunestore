package com.tunestore.action;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.tunestore.util.DBUtil;
import com.tunestore.util.IWithDataSource;

public class PasswordAction extends Action implements IWithDataSource {
  private DataSource dataSource;
  
  public static final String ERROR_KEY = "com.tunestore.action.PasswordAction.ERROR_KEY";
  public static final String MESSAGE_KEY = "com.tunestore.action.PasswordAction.MESSAGE_KEY";
  
  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }
  
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    DynaActionForm df = (DynaActionForm)form;
    
    ActionMessages errors = (ActionMessages)request.getAttribute(ERROR_KEY);
    if (errors == null) {
      errors = new ActionMessages();
    }
    ActionMessages messages = (ActionMessages)request.getAttribute(MESSAGE_KEY);
    if (messages == null) {
      messages = new ActionMessages();
    }
    
    if ((null == df.get("password")) || "".equals(df.get("password"))) {
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.required", "prompt.password"));
    } else if (! df.get("password").equals(df.get("rptpass"))) {
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.nomatch"));
    } else {
      Connection conn = null;
      
      try {
        conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement("UPDATE TUNEUSER SET PASSWORD = ?"
            + "WHERE USERNAME = ?");
        stmt.setString(1, request.getParameter("password"));
        stmt.setString(2, (String)request.getSession(true).getAttribute("USERNAME"));
        stmt.executeUpdate();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("password.changed"));
      } catch (Exception e) {
        throw new RuntimeException(e);
      } finally {
        if (conn != null) {
          try { conn.close(); } catch (Exception e) {}
        }
      }
    }
    
    request.setAttribute(ERROR_KEY, errors);
    request.setAttribute(MESSAGE_KEY, messages);
    return mapping.findForward("success");
  }
}
