package com.demo.www.test.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import core.util.CommUtils;

public final class Util {
  public static final String key = "user_id_2015_key";
    
  public static String encodeDes( String value ) {
    return DES.encodeOneKey(value, DES.key);
  }
  
  public static String decodeDes( String value ) {
    return DES.decodeOneKey(value, DES.key);
  }
  
  /**
   * 获得文件大小
   * @param f
   * @return
   */
  public static String getFileSize( File f ) {
    String resource_size = "";
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(f);
      DecimalFormat df = new DecimalFormat("#.##");
      double resourcesize = (double)((double)fis.available()/1024);
      if( resourcesize > 1000 ) {
          resource_size =df.format((double)((double)fis.available()/1024/1024))+"MB";
      } else {
          resource_size =df.format((double)((double)fis.available()/1024))+"KB";
      }
    } catch ( Exception e) {
      e.printStackTrace();
      return "未找到";
    } finally {
      if ( fis != null ) {
        try {
          fis.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return resource_size;
  }
  
  /**
   * 获得文件大小
   * @param f
   * @return
   */
  public static String getFileSize(  Long fsize ) {
    String resource_size = "";
    try {
      DecimalFormat df = new DecimalFormat("#.##");
      double size = fsize.doubleValue();
      double resourcesize = (double)(size/1024);
      if( resourcesize > 1000 ) {
          resource_size =df.format((double)( resourcesize /1024))+"MB";
      } else {
          resource_size =df.format((double)( resourcesize ))+"KB";
      }
    } catch ( Exception e) {
      e.printStackTrace();
      return "";
    }  
    return resource_size;
  }
  
 
 
  
  /**
   * 验证身份证
   * @param idNumber
   * @return
   */
  public static boolean isIdNumber( String idNumber ) {
    Pattern idNumPattern = Pattern.compile("^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$");  
    Matcher idNumMatcher = idNumPattern.matcher(idNumber);
    return idNumMatcher.matches();
  }
  
  public static void main(String[] args) {
  }
  
  /**  
   * <PRE>  
   * 半角字符->全角字符转换    
   * 只处理空格，!到˜之间的字符，忽略其他  
   * </PRE>  
   */    
  public static String bj2qj(String src) {    
      if (src == null) {    
          return src;    
      }    
      StringBuilder buf = new StringBuilder(src.length());    
      char[] ca = src.toCharArray();    
      for (int i = 0; i < ca.length; i++) {    
        if ( ca[i] == 34 || ca[i] == 39 || ca[i] == 60 || ca[i] == 62) {//34 ", 39 ', 60 <, 62 > 
          buf.append((char) (ca[i] + 65248)); 
        } else {
          buf.append((char) (ca[i])); 
        }
      }    
      return buf.toString();    
  }   
  /**
   * 获取IP地址
   * @param request
   * @return
   */
  public static String getIpAddr(HttpServletRequest request) {
    if (null == request) {
      return null;
    }
    String proxs[] = { 
        "X-Forwarded-For"
      , "Proxy-Client-IP"
      , "WL-Proxy-Client-IP"
      , "HTTP_CLIENT_IP"
      , "HTTP_X_FORWARDED_FOR" 
      ,"x-real-ip" 
    };
    String ip = null;
    for (String prox : proxs) {
      ip = request.getHeader(prox);
      if (CommUtils.isNull(ip) || "unknown".equalsIgnoreCase(ip)) {
        continue;
      } else {
        break;
      }
    }
    if ( CommUtils.isNull(ip) ) {
      return request.getRemoteAddr();
    }
    return ip;
 }
}
