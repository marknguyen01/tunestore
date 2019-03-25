package com.tunestore.action;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

public class DownloadAction extends Action {
  private static final Log log = LogFactory.getLog(DownloadAction.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    DynaActionForm daf = (DynaActionForm)form;
    
    try {
      // Try to open the stream first - if there's a goof, it'll be here
      InputStream is = this.getServlet().getServletContext().getResourceAsStream("/WEB-INF/bits/" + request.getParameter("cd"));
      
      if (is != null) {
        response.setContentType("audio/mpeg");
        response.setHeader("Content-disposition", "attachment; filename=" + daf.getString("cd"));    
        byte[] buff = new byte[4096];
        int bread = 0;
        while ((bread = is.read(buff)) >= 0) {
          response.getOutputStream().write(buff, 0, bread);
        }
      } else {
        ActionMessages errors = getErrors(request);
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("download.error"));
        saveErrors(request, errors);
        return mapping.findForward("error");        
      }
    } catch (Exception e) {
      e.printStackTrace();
      ActionMessages errors = getErrors(request);
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("download.error"));
      saveErrors(request, errors);
      return mapping.findForward("error");
    }
    
    return null;
  }
}
