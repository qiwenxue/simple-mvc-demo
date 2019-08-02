package com.demo.www.test.entity;

import java.io.Serializable;

import core.annotation.Column;
import core.annotation.Table;
@SuppressWarnings("serial")
@Table(name="sys_user")
public class SysUser implements Serializable {
  @Column(name="id", id=true)
  private String id;
  @Column(name="org_id")
  private String orgId;
  @Column(name="org_code")
  private String orgCode;
  @Column(name="user_code")
  private String userCode;
  @Column(name="pwd")
  private String pwd;
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
  public String getUserCode() {
    return userCode;
  }
  public void setUserCode(String userCode) {
    this.userCode = userCode;
  }
  public String getPwd() {
    return pwd;
  }
  public void setPwd(String pwd) {
    this.pwd = pwd;
  }
  
  
}
