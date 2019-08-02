package com.demo.www.test.service;

import java.sql.Connection;
import java.util.List;

import com.demo.www.test.entity.SysCustomer;
import com.demo.www.test.entity.SysUser;

import core.BaseService;
import core.ORM;

public class CustService extends BaseService {

  public List<SysCustomer> find(Connection con, SysUser u) throws Exception {
    ORM orm = new ORM( new SysCustomer());
    StringBuilder sb = new StringBuilder();
    sb.append( orm.getSelectSQL() );
    sb.append( " where org_code=? ");
    return this.dao.queryBeans(con, sb.toString(), SysCustomer.class, new Object[]{u.getOrgCode()});
  }

  @Override
  public void initDao() {
    // TODO Auto-generated method stub
    
  }
}
