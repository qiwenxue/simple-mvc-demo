package com.demo.www.test.entity;

import java.io.Serializable;

import core.annotation.Column;

public class B implements Serializable {
  @Column(name="test_col")
  private String testCol;

  private int testCol2;
    
  public int getTestCol2() {
    return testCol2;
  }

  public void setTestCol2(int testCol2) {
    this.testCol2 = testCol2;
  }

  public String getTestCol() {
    return testCol;
  }

  public void setTestCol(String testCol) {
    this.testCol = testCol;
  }

  
    
}
