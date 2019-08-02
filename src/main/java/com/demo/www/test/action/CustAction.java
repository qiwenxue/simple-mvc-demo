package com.demo.www.test.action;

import java.sql.Connection;
import java.util.List;

import com.demo.www.test.base.ActionSupport;
import com.demo.www.test.entity.SysCustomer;
import com.demo.www.test.entity.SysUser;
import com.demo.www.test.service.CustService;

import core.annotation.Action;
import core.annotation.Method;
import core.annotation.Service;
import core.db.DBConnectionUtil;
import net.sf.json.JSONArray;

@Action(namespace="cst")
public class CustAction extends ActionSupport {
  @Service
  private CustService custService;
  
  @Method(name="fs", type=Method.POST)
  public String find() throws Exception {
    Connection con = null;
    SysUser u = null;
    try {
      con = DBConnectionUtil.getDBConnection();
      u = this.getUser();
      List<SysCustomer> custs = custService.find(con, u);
      JSONArray array = JSONArray.fromObject(custs);
      setResult(0, array.toString(), "查询成功");
    } catch (Exception e) {
      throw new Exception("查询时异常",e);
    } finally {
      DBConnectionUtil.close(con);
    }
    return SUCCESS;
  }
}
