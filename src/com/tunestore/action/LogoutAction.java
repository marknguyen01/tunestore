package com.tunestore.action;

import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.tunestore.util.IWithDataSource;

public class LogoutAction extends Action implements IWithDataSource {
  private DataSource dataSource;
  
  private static final Log log = LogFactory.getLog(LogoutAction.class);
  
  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }
  
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    // Remove their persistence cookie if they have it
    Cookie[] cookies = request.getCookies();
    if(cookies != null) {
        for (int i = 0; i < cookies.length; i++) {
            if ("persistenttoken".equals(cookies[i].getName())) {
              Connection conn = null;
              
              try {
                conn = dataSource.getConnection();
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("DELETE FROM PERSISTENTLOGIN "
                    + "WHERE TOKEN = '"
                    + cookies[i].getValue()
                    + "'");
                log.info("Removed token for " + request.getSession(true).getAttribute("USERNAME"));
              } catch (Exception e) {
                throw e;
              } finally {
                if (conn != null) {
                  try { conn.close(); } catch (Exception e) {}
                }
              }
              
              Cookie newCookie = new Cookie("persistenttoken","");
              newCookie.setMaxAge(0);
              response.addCookie(newCookie);
            }
        }
    }


    request.getSession(true).invalidate();
    return mapping.findForward("index");
  }
}
