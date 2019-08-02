package com.demo.www.test.action;

import java.sql.Connection;
import java.util.UUID;

import com.demo.www.test.entity.SysUser;
import com.demo.www.test.service.SysService;
import com.demo.www.test.util.Const;
import com.demo.www.test.util.CookieUtil;
import com.demo.www.test.util.Cache;

import core.BaseAction;
import core.annotation.Action;
import core.annotation.Method;
import core.annotation.Param;
import core.annotation.Service;
import core.db.DBConnectionUtil;
import core.util.CommUtils;
import net.sf.json.JSONObject;
@Action(namespace="sys")
public class SysAction extends BaseAction {
  @Param(name="su")
  private SysUser user;
  @Service
  private SysService sysService;
  
  /**
   * 登陆
   * @return
   * @throws Exception
   */
  @Method(name="login", type=Method.POST)
  public String login() throws Exception {
    Connection con = null;
    SysUser u = null;
    if ( CommUtils.isNull(user.getUserCode())) {
      setResult(1, "登录名不能为空");
      return ERROR;
    }
    if ( CommUtils.isNull(user.getPwd())) {
      setResult(1, "密码不能为空");
      return ERROR;
    }
    if ( CommUtils.isNull(user.getOrgCode())) {
      setResult(1, "orgId不能为空");
      return ERROR;
    }
    try {
      con = DBConnectionUtil.getDBConnection();
      u = sysService.authLogin(con, user);
    } catch (Exception e) {
      throw new Exception("验证登陆时异常",e);
    } finally {
      DBConnectionUtil.close(con);
    }
    if ( u == null ) {
      setResult(1, "用户不存在");
      return ERROR;
    } else {
      String passwd = u.getPwd();
      if (! user.getPwd().equals(passwd) ) {
        setResult(1, "密码不正确");
        return ERROR;
      } else {
        String loginToken = UUID.randomUUID().toString();
        String val = JSONObject.fromObject(u).toString();
        CookieUtil.addCookie(request, response, Const.LOGIN.loginToken, loginToken);//返回页面随机数
        Cache.set(loginToken, val);//保存登陆信息到Redis
        //setResult(0, "查询成功");
        JSONObject data = JSONObject.fromObject(u);
        setResult(0, data, "登录成功");
      }
    }
    return SUCCESS;
  }
}
