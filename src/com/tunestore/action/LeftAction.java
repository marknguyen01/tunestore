package com.tunestore.action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.tunestore.util.IWithDataSource;

public class LeftAction extends Action implements IWithDataSource {
  private DataSource dataSource;
  
  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    if (request.getSession(true).getAttribute("USERNAME") != null) {
      Connection conn = null;
      
      try {
        conn = dataSource.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT balance FROM tuneuser WHERE USERNAME = '"
        		+ request.getSession(true).getAttribute("USERNAME")
        		+ "'");
        if (rs.next()) {
          Double balance = new Double(rs.getDouble(1));
          request.getSession(true).setAttribute("BALANCE", balance);
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      } finally {
        if (conn != null) {
          try { conn.close(); } catch (Exception e) {}
        }
      }

      return mapping.findForward("logged");
    } else {
      return mapping.findForward("anonymous");
    }
  }
}
