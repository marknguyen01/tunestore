package com.tunestore.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.tunestore.util.IWithDataSource;

public class LeaveCommentAction extends Action implements IWithDataSource {
  private DataSource dataSource;
  private static final Log log = LogFactory.getLog(LeaveCommentAction.class);
  
  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    ActionForward forward = mapping.findForward("success");
    ActionMessages messages = getMessages(request);
    ActionMessages errors = getErrors(request);
    DynaActionForm daf = (DynaActionForm)form;
    Connection conn = null;
    
    try {
      conn = dataSource.getConnection();
      PreparedStatement stmt = conn.prepareStatement("INSERT INTO COMMENT"
          + "(CD,TUNEUSER,COMMENT) VALUES (?,?,?)");
      stmt.setString(1, daf.getString("cd"));
      stmt.setString(2, (String)request.getSession(true).getAttribute("USERNAME"));
      stmt.setString(3, daf.getString("comment"));
      stmt.executeUpdate();
      
    } catch (SQLException sqle) {
      log.error(sqle);
      throw sqle;
    } finally {
      if (conn != null) {
        try {conn.close();} catch (Exception e) {}
      }
    }
    
    saveMessages(request, messages);
    saveErrors(request, errors);
    return forward;
  }
}
