package com.demo.www.test.entity;

import java.io.Serializable;

import core.annotation.Column;
import core.annotation.Table;
@Table(name="sys_customer")
public class SysCustomer implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 4815097849102007873L;
  @Column(name="id", id=true)
  private String id;
  @Column(name="org_id")
  private String orgId;
  @Column(name="org_code")
  private String orgCode;
  @Column(name="cust_code")
  private String custCode;
  @Column(name="cust_name")
  private String custName;
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getOrgId() {
    return orgId;
  }
  public void setOrgId(String orgId) {
    this.orgId = orgId;
  }
  public String getOrgCode() {
    return orgCode;
  }
  public void setOrgCode(String orgCode) {
    this.orgCode = orgCode;
  }
  public String getCustCode() {
    return custCode;
  }
  public void setCustCode(String custCode) {
    this.custCode = custCode;
  }
  public String getCustName() {
    return custName;
  }
  public void setCustName(String custName) {
    this.custName = custName;
  }
  
  
}
