package com.demo.www.test.util;

import jedis.JedisUtil;
public final class Cache {
  private static JedisUtil userMap  = JedisUtil.getInstance();
    
  public static final String get(String key) throws Exception {
   return userMap.get(key); 
  }
  
  public static final void set(String key, String val) throws Exception {
    userMap.set(key, val);
  }
}
