package com.tunestore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.tunestore.util.DBUtil;

public class ViewCDsAction extends Action {
  private static final Log log = LogFactory.getLog(ViewCDsAction.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    try {
      log.info("Getting CD's");
      List results = DBUtil.getCDsForUser((String)request.getSession(true).getAttribute("USERNAME"), request.getParameter("friend"));
      request.setAttribute("CDLIST", results);
      request.setAttribute("SHOWVIEW", "viewcds");
      request.setAttribute("TITLE", "Now viewing CD's for " + request.getParameter("friend"));
    } catch (Exception e) {
      e.printStackTrace(response.getWriter());
      throw e;
    }
    
    return mapping.findForward("success");
  }
}
