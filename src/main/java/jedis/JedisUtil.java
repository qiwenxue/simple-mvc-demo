package jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import core.util.CommUtils;
import redis.clients.jedis.Jedis;

public class JedisUtil {
  
  private static JedisUtil jedisUtil;
  
  private static final int EXPIRE_TIME = 60; //20分钟操作，就过期
  
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
   * 存入某条记录
   * @param key
   * @param value
   */
  public void set(String key, String value){
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
   * 存入某条记录
   * @param tableKey
   * @param valueKey
   * @param object
   */
  public void put(String tableKey, String valueKey, Object object ) {
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
   * 存入缓存，带过期时间
   * @param tableKey
   * @param valueKey
   * @param object
   * @param expireTime
   */
  public void put( String tableKey, String valueKey, Object object, int expireTime ) {
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
   * 不过期
   */
  public void putWithNoExpireTime( String tableKey, String valueKey, Object object) {
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
   * 获取某条记录
   * @param tableKey
   * @param objectKey
   * @return
   * @throws Exception
   */
  public Object get(String tableKey, String objectKey) throws Exception {
    Jedis jedis = RedisPoolUtil.getJedis();
    if ( CommUtils.isNull(tableKey) ) {
      throw new Exception("参数tableKey不能为空");
    }
    if ( CommUtils.isNull(objectKey) ) {
      throw new Exception("参数objectKey不能为空");
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
   * 获取表table里的所有的数据
   * @param tableKey
   * @throws Exception 
   */
  public List<Object> getAll( String tableKey ) throws Exception {
    List<Object> objs = new ArrayList<Object>(); 
    if ( CommUtils.isNull(tableKey) ) {
      throw new Exception("参数tableKey不能为空");
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
   * 查找表tableKey里一共有多少数据
   * @param tableKey
   * @return
   * @throws Exception
   */
  public int getSize( String tableKey ) throws Exception {
    if ( CommUtils.isNull(tableKey) ) {
      throw new Exception("参数tableKey不能为空");
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
   * 查看是否存在key
   * @param tableKey
   * @param objectKey
   * @return
   */
  public boolean containsKey(String tableKey, String objectKey) {
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
   * 删除某个记录
   * @param tableKey
   * @param objectKey
   */
  public void remove(String tableKey, String objectKey) {
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
   * 清除对应table的Jedis数据
   * @param tableKey
   */
  public void clearTable(String tableKey) {
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
   * 设置key的过期时间
   * @param key
   * @param seconds 单位：秒
   */
	public void expire(String key, String value, int seconds) {
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
	 * 根据key，获取value值
	 * @param key
	 * @return
	 */
	public String get(String key) {
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
