package com.demo.www.test.action;

import java.sql.Connection;

import com.demo.www.test.entity.User;
import com.demo.www.test.service.UserService;

import core.BaseAction;
import core.annotation.Action;
import core.annotation.Method;
import core.annotation.Param;
import core.annotation.Service;
import core.db.DBConnectionUtil;
import net.sf.json.JSONObject;
@Action(namespace="user")
public class UserAction extends BaseAction {
  @Service
  private UserService userService;
  @Param(name="u")
  private User user;
  
  @Method(name="saveUser")
  public String save() throws Exception {
    Connection con = null;
    try {
      con = DBConnectionUtil.getDBConnection();
      int id = userService.save(con, user);
      if ( id >0 ) {
        setResult(0, "成功");
        return SUCCESS;
      } else {
        setResult(1, "失败");
        return ERROR;
      }
    } catch (Exception e) {
      e.printStackTrace();
      DBConnectionUtil.rollBack(con);//回滚事务
      throw new Exception("保存用户异常",e);
    } finally {
      DBConnectionUtil.close(con);
    }
  }
  
  @Method(name="findUser", type=Method.GET)
  public String find() throws Exception {
    Connection con = null;
    try {
      con = DBConnectionUtil.getDBConnection();
      User us = userService.findUser(con, user.getId());
      if ( us != null ) {
        JSONObject data = JSONObject.fromObject(us);
        setResult(0, data, "成功");
        return SUCCESS;
      } else {
        setResult(1, "失败");
        return ERROR;
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("查询用户异常",e);
    } finally {
      DBConnectionUtil.close(con);
    }
  }
}
