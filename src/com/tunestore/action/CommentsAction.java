package com.tunestore.action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;

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
import com.tunestore.beans.Comment;
import com.tunestore.util.IWithDataSource;

public class CommentsAction extends Action implements IWithDataSource {
  private DataSource dataSource;
  private static final Log log = LogFactory.getLog(CommentsAction.class);
  
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
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT COMMENT.TUNEUSER, "
          + "COMMENT.COMMENT "
          + "FROM COMMENT "
          + "WHERE COMMENT.CD = "
          + daf.getString("cd"));
      ArrayList comments = new ArrayList();
      while (rs.next()) {
        Comment comment = new Comment();
        comment.setLeftby(rs.getString(1));
        comment.setComment(rs.getString(2));
        comments.add(comment);
      }
      request.setAttribute("comments", comments);
      
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
    } catch (SQLException sqle) {
      log.error(sqle.getMessage());
      throw sqle;
    } finally {
      if (conn != null) {
        try { conn.close(); } catch(Exception e) {}
      }
    }
    
    saveMessages(request, messages);
    saveErrors(request, errors);
    
    if ("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
      forward = mapping.findForward("ajax");
    } else {
      log.info("RW: " + request.getHeader("x-requested-with"));
    }
    
    return forward;
  }
}
