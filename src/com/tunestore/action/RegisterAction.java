package com.tunestore.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

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
import org.apache.struts.util.MessageResources;

import com.tunestore.util.IWithDataSource;

public class RegisterAction extends Action implements IWithDataSource {
  private DataSource dataSource;

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    DynaActionForm daf = (DynaActionForm)form;
    ActionMessages errors = new ActionMessages();
    ActionForward forward = mapping.getInputForward();
    MessageResources resources = getResources(request);
    
    if (null == daf.getString("username") || "".equals(daf.getString("username"))) {
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.required", resources.getMessage("prompt.username")));
    }
    if (null == daf.getString("password") || "".equals(daf.getString("password"))) {
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.required", resources.getMessage("prompt.password")));
    }
    if (null == daf.getString("rptpass") || "".equals(daf.getString("rptpass"))) {
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.required", resources.getMessage("prompt.rpt")));
    }
    if ((null != daf.getString("password")) && (null != daf.getString("rptpass"))) {
      if (! daf.getString("password").equals(daf.getString("rptpass"))) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.rptpass.nomatch"));
      }
    }
    
    Connection conn = null;
    try {
      conn = dataSource.getConnection();
      PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) USERCNT "
              + "FROM TUNEUSER "
              + "WHERE USERNAME=?");
      stmt.setString(1, daf.getString("username"));
      ResultSet rs = stmt.executeQuery();
      rs.next();
      if (rs.getInt("USERCNT") > 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.exists"));
      } else if (errors.isEmpty()) {
        PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO TUNEUSER (USERNAME,PASSWORD,BALANCE) VALUES (?, ?, 0.00)");
        stmt2.setString(1, daf.getString("username"));
        stmt2.setString(2, daf.getString("password"));
        stmt2.executeQuery();
        
        ActionMessages msgs = getMessages(request);
        msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("user.added"));
        forward = mapping.findForward("success");
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (conn != null) {
        try { conn.close(); } catch (Exception e) {}
      }
    }
    
    request.setAttribute("ERRORS.REGISTER", errors);
    return forward;
  }
}
