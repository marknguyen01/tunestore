package com.tunestore.action;

import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.tunestore.form.AddBalanceForm;
import com.tunestore.util.DBUtil;
import com.tunestore.util.IWithDataSource;

public class AddBalanceAction extends Action implements IWithDataSource {
  private DataSource dataSource;
  
  private static final Log log = LogFactory.getLog(AddBalanceAction.class);

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }
  
  private boolean isValidCC(String cc) {
    boolean retval = false;
    /*
    int total = 0;
    for (int i = 0; i < cc.length(); i++) {
      if (i % 2 == 0) {
        log.info("Adding " + (Integer.parseInt(cc.substring(cc.length() - i - 1, cc.length() - i))));
        total += Integer.parseInt(cc.substring(cc.length() - i - 1, cc.length() - i));
      } else {
        int addend = 2 * Integer.parseInt(cc.substring(cc.length() - i - 1, cc.length() - i));
        if (addend > 9) {
          addend -= 9;
          log.info("> 9 " + addend);
        }
        log.info("Adding " + addend);
        total += addend;
      }
    }
    log.info("Total " + total);
    retval = (total % 10 == 0);
    */
    retval =(cc.length() == 16); // No check-sum
    
    return retval;
  }

  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    response.setDateHeader("Expires", -1);
    
    AddBalanceForm abf = (AddBalanceForm)form;
    ActionMessages errors = getErrors(request);
    ActionMessages messages = getMessages(request);
    ActionForward retval = mapping.findForward("success");
    
    String re = "^5[12345]\\d{14}$";
    if ("VISA".equals(abf.getVendor())) {
      re = "^4\\d{12}(\\d{3})?$";
    }
    
    re = "\\d{16}$"; // Accepts all 16 digit numbers
    
    if (null == abf.getCc()) {
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.required", "Card Number"));
    } else {
      if (! abf.getCc().matches(re)) {
        log.info("cc" + abf.getCc() + " doesn't match re");
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.creditcard", "Card Number"));
      } else {
        if (! isValidCC(abf.getCc())) {
          log.info("cc" + abf.getCc() + " doesn't checksum");
          errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.creditcard", "Card Number"));
        } else {
          Connection conn = null;
          
          try {
            conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "UPDATE TUNEUSER SET BALANCE = BALANCE + "
              + abf.getAmount()
              + " WHERE USERNAME = '"
              + request.getSession(true).getAttribute("USERNAME")
              + "'";
            log.info(sql);
            stmt.executeUpdate(sql);
            
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("balance.added"));
          } catch (Exception e) {
            e.printStackTrace();
            throw e;
          } finally {
            if (conn != null) {
              try { conn.close(); } catch (Exception e) {}
            }
          }
        }
      }
    }
    
    saveErrors(request, errors);
    saveMessages(request, messages);
    return retval;
  }
}
