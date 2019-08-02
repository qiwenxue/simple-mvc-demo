package com.demo.www.test.base;

import com.demo.www.test.entity.SysUser;
import com.demo.www.test.util.Cache;
import com.demo.www.test.util.Const;
import com.demo.www.test.util.CookieUtil;

import core.BaseAction;
import net.sf.json.JSONObject;

public class ActionSupport extends BaseAction {
  
  public SysUser getUser() throws Exception {
    String token = CookieUtil.getCookieValue(request, Const.LOGIN.loginToken);
    String str = Cache.get(token);
    JSONObject obj = JSONObject.fromObject(str);
    SysUser user = (SysUser)JSONObject.toBean(obj, SysUser.class);
    return user;
  }
  
}
