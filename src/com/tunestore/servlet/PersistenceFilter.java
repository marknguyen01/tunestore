package com.tunestore.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tunestore.util.DBUtil;

public class PersistenceFilter implements Filter {
  private static final Log log = LogFactory.getLog(PersistenceFilter.class);

  public void destroy() {
  }

  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest)request;
    if (null != httpRequest.getSession(true).getAttribute("USERNAME")) {
      chain.doFilter(request, response);
      return;
    }
    
    Cookie[] cookies = httpRequest.getCookies();
    
    if (cookies == null) {
      chain.doFilter(request, response);
      return;
    }
    
    for (int i = 0; i < cookies.length; i++) {
      if ("persistenttoken".equals(cookies[i].getName())) {
        String token = cookies[i].getValue();
        
        // We have a persistent token cookie, so see if this maps to
        // a real user
        Connection conn = null;
        
        try {
          log.info("User sent token " + token);
          conn = DBUtil.getConnection();
          Statement stmt = conn.createStatement();
          ResultSet rs = stmt.executeQuery("SELECT APP.TUNEUSER.USERNAME,"
              + "APP.TUNEUSER.BALANCE "
              + "FROM APP.TUNEUSER "
              + "INNER JOIN APP.PERSISTENTLOGIN "
              + "ON APP.PERSISTENTLOGIN.TUNEUSER = TUNEUSER.USERNAME "
              + "WHERE APP.PERSISTENTLOGIN.TOKEN = '"
              + token
              + "'");
          if (rs.next()) {
            httpRequest.getSession(true).setAttribute("USERNAME", rs.getString("USERNAME"));
            httpRequest.getSession(true).setAttribute("BALANCE", rs.getString("BALANCE"));
            log.info("User " + httpRequest.getSession(true).getAttribute("USERNAME") + " logged in from persistence");
          }
        } catch (Exception e) {
        } finally {
          if (conn != null) {
            try { conn.close(); } catch (Exception e) {}
          }
        }
      }
    }
    
    chain.doFilter(request, response);
  }

  public void init(FilterConfig arg0) throws ServletException {
  }
}
