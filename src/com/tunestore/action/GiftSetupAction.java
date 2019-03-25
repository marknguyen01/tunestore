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
import org.apache.struts.action.DynaActionForm;

import com.tunestore.beans.CD;
import com.tunestore.util.IWithDataSource;

public class GiftSetupAction extends Action implements IWithDataSource {
  private DataSource dataSource;
  
  private static final Log log = LogFactory.getLog(GiftSetupAction.class);

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

    try {
      conn = dataSource.getConnection();
      Statement stmt = conn.createStatement();
      String sql = "SELECT FRIEND.USERNAME "
        + "FROM TUNEUSER "
        + "INNER JOIN FRIENDSHIP "
        + "ON FRIENDSHIP.TUNEUSER1 = TUNEUSER.USERNAME "
        + "INNER JOIN TUNEUSER FRIEND "
        + "ON FRIEND.USERNAME = FRIENDSHIP.TUNEUSER2 "
        + "LEFT JOIN TUNEUSER_CD "
        + "ON TUNEUSER_CD.TUNEUSER = FRIEND.USERNAME "
        + "AND TUNEUSER_CD.CD = "
        + daf.getString("cd")
        + " WHERE TUNEUSER.USERNAME = '"
        + request.getSession(true).getAttribute("USERNAME")
        + "' AND TUNEUSER_CD.CD IS NULL";
      ResultSet rs = stmt.executeQuery(sql);
      log.info(sql);
      List friends = new ArrayList();
      while (rs.next()) {
        friends.add(rs.getString(1));
      }

      // And get the cd information
      rs.close();
      rs = stmt.executeQuery("SELECT ID, ARTIST, ALBUM, IMAGE, PRICE, BITS "
          + "FROM CD " + "WHERE ID = " + request.getParameter("cd"));
      if (rs.next()) {
        CD cd = new CD();
        cd.setId(new Long(rs.getLong(1)));
        cd.setArtist(rs.getString(2));
        cd.setAlbum(rs.getString(3));
        cd.setImage(rs.getString(4));
        cd.setPrice(new Double(rs.getDouble(5)));
        cd.setBits(rs.getString(6));
        request.setAttribute("cd", cd);
      }

      request.setAttribute("SHOWVIEW", "friendgift");
      request.setAttribute("FRIENDS", friends);
      request.setAttribute("TITLE", "Give a Gift of Music to...");
      return retval;
    } catch (Exception e) {
      throw new RuntimeException(e);
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
