package com.demo.www.test.util;

import jedis.JedisUtil;
public final class Cache {
  private static JedisUtil userMap  = JedisUtil.getInstance();
    
  public static final String get(String key) {
   return userMap.get(key); 
  }
  
  public static final void set(String key, String val) {
    userMap.set(key, val);
  }
}
