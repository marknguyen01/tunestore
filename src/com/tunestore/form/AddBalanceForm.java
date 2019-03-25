package com.tunestore.form;

import org.apache.struts.action.ActionForm;

public class AddBalanceForm extends ActionForm {
  private static final long serialVersionUID = -7459319274669027914L;
  private String vendor;
  private String cc;
  private Double amount;
  public String getVendor() {
    return vendor;
  }
  public void setVendor(String vendor) {
    this.vendor = vendor;
  }
  public String getCc() {
    return cc;
  }
  public void setCc(String cc) {
    this.cc = cc;
  }
  public Double getAmount() {
    return amount;
  }
  public void setAmount(Double amount) {
    this.amount = amount;
  }
}
