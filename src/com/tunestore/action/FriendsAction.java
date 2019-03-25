package com.tunestore.action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

import com.tunestore.beans.Friend;
import com.tunestore.util.IWithDataSource;

public class FriendsAction extends Action implements IWithDataSource {
  private DataSource dataSource;
  
  private static final Log log = LogFactory.getLog(FriendsAction.class);

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }
  
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    ActionForward retval = mapping.findForward("success");
    ActionMessages errors = getErrors(request);
    ActionMessages messages = getMessages(request);
    
    Connection conn = null;
    
    try {
      conn = dataSource.getConnection();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT TUNEUSER2, APPROVED "
          + "FROM FRIENDSHIP "
          + "WHERE TUNEUSER1 = '"
          + request.getSession(true).getAttribute("USERNAME")
          + "'");
      List myfriends = new ArrayList();
      while (rs.next()) {
        Friend friend = new Friend();
        friend.setUsername(rs.getString(1));
        friend.setApproved("Y".equals(rs.getString(2)));
        myfriends.add(friend);
      }
      request.setAttribute("myfriends", myfriends);
      
      String sql = "SELECT TUNEUSER1 "
        + "FROM FRIENDSHIP "
        + "WHERE TUNEUSER2 = '"
        + request.getSession(true).getAttribute("USERNAME")
        + "' AND APPROVED = 'N'";
      log.info(sql);
      rs = stmt.executeQuery(sql);
      List requests = new ArrayList();
      while (rs.next()) {
        Friend friend = new Friend();
        friend.setUsername(rs.getString(1));
        friend.setApproved(false);
        requests.add(friend);
      }
      request.setAttribute("requests", requests);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (conn != null) {
        try { conn.close(); } catch (Exception e) {}
      }
    }
    
    request.setAttribute("SHOWVIEW", "friends");
    saveMessages(request, messages);
    saveErrors(request, errors);
    return retval;
  }
}
