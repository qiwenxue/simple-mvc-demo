package com.demo.www.test.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.demo.www.test.entity.User;

import core.BaseService;
import core.ORM;

public class UserService extends BaseService {

  public int save( Connection con, User u) throws Exception {
    u.setTestCol("aaa");
    return this.dao.saveBean(con, u);
  }
  
  public int delete(Connection con, int userId) throws Exception {
    StringBuffer sql = new StringBuffer();
    ORM orm = new ORM( new User());
    sql.append( orm.getDeleteSQL()+" where id=? ");
    return this.dao.deleteBean(con, sql.toString(), new Object[]{ userId });
  }
  
  public User findUser( Connection con, int id ) throws Exception {
    StringBuffer sql = new StringBuffer();
    ORM orm = new ORM( new User());
    sql.append( orm.getSelectSQL() );
    sql.append(" where id=? ");
    return this.dao.queryBean(con, sql.toString(), User.class, new Object[]{id});
  }
  
  public List<User> findUsers( Connection con ) throws Exception{
    StringBuffer sql = new StringBuffer();
    ORM orm = new ORM( new User());
    sql.append( orm.getSelectSQL() +" ");
    return this.dao.queryBeans(con, sql.toString(), User.class);
  }
  
  public void saveBench( Connection con ) throws Exception {
     List<User> objs = new ArrayList<User>();
     for (int i=0; i<100000; i++) {
       User u = new User();
       u.setUserName("test_"+i);
       u.setUserAge(i);
       objs.add(u);
     }
     List<Object> o = new ArrayList<Object>();
     o.addAll(objs);
     this.dao.saveBeans(con, o, 2000);
  }

  @Override
  public void initDao() {
    // TODO Auto-generated method stub
    
  }
    
}
