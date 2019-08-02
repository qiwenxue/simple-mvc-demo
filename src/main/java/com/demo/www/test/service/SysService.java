package com.demo.www.test.service;

import java.sql.Connection;

import com.demo.www.test.entity.SysUser;

import core.BaseService;
import core.ORM;

public class SysService extends BaseService {
  
  public SysUser authLogin(Connection con, SysUser user) throws Exception {
    ORM orm = new ORM( user );
    StringBuilder sb = new StringBuilder();
    sb.append( orm.getSelectSQL() );
    sb.append(" where org_code=? and user_code=? ");
    SysUser u = this.dao.queryBean(con, sb.toString(), SysUser.class, new Object[]{user.getOrgCode(), user.getUserCode()});
    return u;
  }

  @Override
  public void initDao() {
    // TODO Auto-generated method stub
    
  }
  
}
