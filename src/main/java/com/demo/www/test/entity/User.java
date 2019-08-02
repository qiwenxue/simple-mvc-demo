package com.demo.www.test.entity;

import java.io.Serializable;

import core.annotation.Column;
import core.annotation.Table;
@Table(name="t_user")
public class User extends B implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -4612232816195353592L;

  @Column(name="id", id=true)
  private int id;
  @Column(name="user_name")
  private String userName;
  private int userAge;
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public int getUserAge() {
    return userAge;
  }
  public void setUserAge(int userAge) {
    this.userAge = userAge;
  }
  
  
}
