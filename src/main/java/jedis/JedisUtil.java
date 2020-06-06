package jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import core.util.CommUtils;
import redis.clients.jedis.Jedis;

public class JedisUtil {
  
  private static JedisUtil jedisUtil;
  
  private static final int EXPIRE_TIME = 60; //20鍒嗛挓鎿嶄綔锛屽氨杩囨湡
  
  private JedisUtil() {}
  
  public static JedisUtil getInstance() {
    if ( jedisUtil == null ) {
      jedisUtil = new JedisUtil();
    }
    return jedisUtil;
  }
  
  public Jedis getJedis() {
    return RedisPoolUtil.getJedis();
  }
  
  /**
   * 瀛樺叆鏌愭潯璁板綍
   * @param key
   * @param value
 * @throws Exception 
   */
  public void set(String key, String value) throws Exception{
    Jedis jedis = RedisPoolUtil.getJedis();
    try {
      jedis.set(key.getBytes(), value.getBytes());
      jedis.expire(key.getBytes(), EXPIRE_TIME);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      RedisPoolUtil.releaseJedis(jedis);
    }
  }
    
  /**
   * 瀛樺叆鏌愭潯璁板綍
   * @param tableKey
   * @param valueKey
   * @param object
 * @throws Exception 
   */
  public void put(String tableKey, String valueKey, Object object ) throws Exception {
    Jedis jedis = RedisPoolUtil.getJedis();
    try {
       jedis.hset(tableKey.getBytes(), valueKey.getBytes(), SerializeUtil.serialize(object));
       jedis.expire(tableKey.getBytes(), EXPIRE_TIME);
    } catch (Exception e) {
       e.printStackTrace();
       throw e;
    } finally {
       RedisPoolUtil.releaseJedis(jedis);
    }
  }
  
  /**
   * 瀛樺叆缂撳瓨锛屽甫杩囨湡鏃堕棿
   * @param tableKey
   * @param valueKey
   * @param object
   * @param expireTime
 * @throws Exception 
   */
  public void put( String tableKey, String valueKey, Object object, int expireTime ) throws Exception {
    Jedis jedis = RedisPoolUtil.getJedis();
    try {
       jedis.hset(tableKey.getBytes(), valueKey.getBytes(), SerializeUtil.serialize(object));
       jedis.expire(tableKey.getBytes(), expireTime);
    } catch (Exception e) {
       e.printStackTrace();
       throw e;
    } finally {
       RedisPoolUtil.releaseJedis(jedis);
    }
  }
  
  /**
   * 涓嶈繃鏈�
 * @throws Exception 
   */
  public void putWithNoExpireTime( String tableKey, String valueKey, Object object) throws Exception {
    Jedis jedis = RedisPoolUtil.getJedis();
    try {
       jedis.hset(tableKey.getBytes(), valueKey.getBytes(), SerializeUtil.serialize(object));
    } catch (Exception e) {
       e.printStackTrace();
       throw e;
    } finally {
       RedisPoolUtil.releaseJedis(jedis);
    }
  }
  
  /**
   * 鑾峰彇鏌愭潯璁板綍
   * @param tableKey
   * @param objectKey
   * @return
   * @throws Exception
   */
  public Object get(String tableKey, String objectKey) throws Exception {
    Jedis jedis = RedisPoolUtil.getJedis();
    if ( CommUtils.isNull(tableKey) ) {
      throw new Exception("鍙傛暟tableKey涓嶈兘涓虹┖");
    }
    if ( CommUtils.isNull(objectKey) ) {
      throw new Exception("鍙傛暟objectKey涓嶈兘涓虹┖");
    }
    try {
      byte[] serObject = jedis.hget(tableKey.getBytes(), objectKey.getBytes());
      return SerializeUtil.unserialize(serObject);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      RedisPoolUtil.releaseJedis(jedis);
    }
  }
  
  /**
   * 鑾峰彇琛╰able閲岀殑鎵�鏈夌殑鏁版嵁
   * @param tableKey
   * @throws Exception 
   */
  public List<Object> getAll( String tableKey ) throws Exception {
    List<Object> objs = new ArrayList<Object>(); 
    if ( CommUtils.isNull(tableKey) ) {
      throw new Exception("鍙傛暟tableKey涓嶈兘涓虹┖");
    }
    Jedis jedis = RedisPoolUtil.getJedis();
    try {
      Map<byte[], byte[]> map = jedis.hgetAll(tableKey.getBytes());
      if ( map != null ) {
        for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {
          byte[] serObject = entry.getValue();
          Object obj = SerializeUtil.unserialize(serObject);
          objs.add(obj);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      RedisPoolUtil.releaseJedis(jedis);
    }
    return objs;
  }
  
  /**
   * 鏌ユ壘琛╰ableKey閲屼竴鍏辨湁澶氬皯鏁版嵁
   * @param tableKey
   * @return
   * @throws Exception
   */
  public int getSize( String tableKey ) throws Exception {
    if ( CommUtils.isNull(tableKey) ) {
      throw new Exception("鍙傛暟tableKey涓嶈兘涓虹┖");
    }
    Jedis jedis = RedisPoolUtil.getJedis();
    try {
      Map<byte[], byte[]> map = jedis.hgetAll(tableKey.getBytes());
      if ( map != null ) {
        return map.size(); 
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      RedisPoolUtil.releaseJedis(jedis);
    }
    return 0;
  }
  
  /**
   * 鏌ョ湅鏄惁瀛樺湪key
   * @param tableKey
   * @param objectKey
   * @return
 * @throws Exception 
   */
  public boolean containsKey(String tableKey, String objectKey) throws Exception {
    Jedis jedis = null;
    try {
      jedis = RedisPoolUtil.getJedis();
      return jedis.hexists(tableKey.getBytes(), objectKey.getBytes());
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      RedisPoolUtil.releaseJedis(jedis);
    }
  }
  
  /**
   * 鍒犻櫎鏌愪釜璁板綍
   * @param tableKey
   * @param objectKey
 * @throws Exception 
   */
  public void remove(String tableKey, String objectKey) throws Exception {
    Jedis jedis = null;
    try {
      jedis = RedisPoolUtil.getJedis();
      jedis.hdel(tableKey.getBytes(), objectKey.getBytes());
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      RedisPoolUtil.releaseJedis(jedis);
    }
  }
  
  /**
   * 娓呴櫎瀵瑰簲table鐨凧edis鏁版嵁
   * @param tableKey
 * @throws Exception 
   */
  public void clearTable(String tableKey) throws Exception {
    Jedis jedis = null;
    try {
      jedis = RedisPoolUtil.getJedis();
      Map<byte[], byte[]> map = jedis.hgetAll(tableKey.getBytes());
      if ( map != null ) {
        jedis.del(tableKey.getBytes());
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      RedisPoolUtil.releaseJedis(jedis);
    }
  }
  
  /**
   * 璁剧疆key鐨勮繃鏈熸椂闂�
   * @param key
   * @param seconds 鍗曚綅锛氱
 * @throws Exception 
   */
	public void expire(String key, String value, int seconds) throws Exception {
		Jedis jedis = RedisPoolUtil.getJedis();
		try {
			jedis.set(key, value);
			if(seconds>0) jedis.expire(key, seconds);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			RedisPoolUtil.releaseJedis(jedis);
		}
	}
	/**
	 * 鏍规嵁key锛岃幏鍙杤alue鍊�
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public String get(String key) throws Exception {
		if(key == null || key.trim().length()==0) return null;
		Jedis jedis = RedisPoolUtil.getJedis();
		try {
			return jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			RedisPoolUtil.releaseJedis(jedis);
		}
	}
}
