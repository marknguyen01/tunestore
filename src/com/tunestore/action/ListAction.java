package com.tunestore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.tunestore.util.DBUtil;

public class ListAction extends Action {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    try {
      List results = DBUtil.getCDsForUser((String)request.getSession(true).getAttribute("USERNAME"), null);
      request.setAttribute("CDLIST", results);
    } catch (Exception e) {
      e.printStackTrace(response.getWriter());
      throw e;
    }
    
    return mapping.findForward("success");
  }
}
