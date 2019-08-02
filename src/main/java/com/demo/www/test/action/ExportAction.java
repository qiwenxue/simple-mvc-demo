package com.demo.www.test.action;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.demo.www.test.entity.User;
import com.demo.www.test.service.UserService;

import core.BaseAction;
import core.annotation.Action;
import core.annotation.Method;
import core.annotation.Service;
import core.db.DBConnectionUtil;
import core.util.CommUtils;
import core.util.ExportExcel;
@Action(namespace="export")
public class ExportAction extends BaseAction {
  @Service
  private UserService userService;
  
  @Method(name="exportDetail", type=Method.POST)
  public String exportDetail() throws Exception {
    Connection con = null;
    OutputStream ops = null;
    List<Map<String, String>> values = new ArrayList<Map<String, String>>();
    try {
      con = DBConnectionUtil.getDBConnection();
      response.reset();
      response.setHeader("Cache-control","private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Content-disposition","attachment; filename=test.xls");
      ops = response.getOutputStream();
      String[] titles = new String[]{"姓名","ID"};
       
      HSSFWorkbook wb = new HSSFWorkbook();
      //select ID AS "id",USER_NAME AS "userName" FROM T_USER limit 1
      List<User> res = userService.findUsers(con);
      if ( CommUtils.isNotNullList(res) ) {
        for ( int i=0; i<res.size(); i++ ) {
          Map<String, String> map = new HashMap<String, String>();
          User user = res.get(i);
          map.put("姓名", user.getUserName());
          map.put("ID", user.getUserAge()+"");
          values.add(map);
        }
      }
      ops = ExportExcel.export2Excel(ops, wb, null, titles, values, "用户列表");
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnectionUtil.close(con);
      values = null;
      try {
        if ( ops != null ) {
          ops.close();
        }
      } catch (Exception e) {
      }
    }
    return null;
  }
  
}
