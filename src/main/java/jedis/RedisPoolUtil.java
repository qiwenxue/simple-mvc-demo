package jedis;

import core.util.CommUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis连接池
 * @author Administrator
 *
 */
public final class RedisPoolUtil {
    
  //Redis服务器ip
  private static String ADDR = "127.0.0.1";
  //redis的端口
  private static int  PORT = 6379; 
  //访问密码
  private static String PASSWORD = null;
  //可以连接的最大数, 默认为8; 如果赋值为-1，则表示不限制； 
  //如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
  private static int MAX_ACTIVE = 1024;
  //一个pool最多有多少个空闲的实例，默认为8。  
  private static int MAX_IDLE = 200;
  //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
  private static int MAX_WAIT = 10000; //最多等待时间
  private static int TIMEOUT = 10000;  //超时时间
  //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
  private static boolean TEST_ON_BORROW = true;
  private static JedisPool jedisPool = null;
  
  static {
    try {
      JedisPoolConfig config = new JedisPoolConfig();
      String address = CommUtils.getPropValByKey("jedis.address", "conf");
      String port = CommUtils.getPropValByKey("jedis.port", "conf");
      String passwd = CommUtils.getPropValByKey("jedis.passwd", "conf");
      String maxActive = CommUtils.getPropValByKey("jedis.max.active", "conf");
      String maxIdle = CommUtils.getPropValByKey("jedis.max.idle", "conf");
      String maxWait = CommUtils.getPropValByKey("jedis.max.wait", "conf");
      String timeout = CommUtils.getPropValByKey("jedis.timeout", "conf");
      String onborrow = CommUtils.getPropValByKey("jedis.test.onbrower", "conf");
      ADDR = CommUtils.isNotNull(address) ? address: ADDR;
      PORT = CommUtils.isNotNull(port)? Integer.parseInt(port) : PORT;
      PASSWORD = CommUtils.isNull(passwd)? PASSWORD : passwd;
      MAX_ACTIVE = CommUtils.isNotNull(maxActive)? Integer.parseInt(maxActive):MAX_ACTIVE;
      MAX_IDLE = CommUtils.isNotNull(maxIdle)? Integer.parseInt(maxIdle):MAX_IDLE;
      MAX_WAIT = CommUtils.isNotNull(maxWait)? Integer.parseInt(maxIdle):MAX_WAIT;
      TIMEOUT = CommUtils.isNotNull(timeout)? Integer.parseInt(maxIdle):TIMEOUT;
      TEST_ON_BORROW = CommUtils.isNotNull(onborrow)? Boolean.parseBoolean(onborrow):TEST_ON_BORROW;
      
      config.setMaxTotal(MAX_ACTIVE);
      config.setMaxIdle(MAX_IDLE);
      config.setMaxWaitMillis(MAX_WAIT);
      
      config.setTestOnBorrow(TEST_ON_BORROW);
      jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, PASSWORD);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  //获取Jedis实例
  public synchronized static Jedis getJedis() {
    if ( jedisPool != null ) {
      Jedis jedis = jedisPool.getResource();
      return jedis;
    }
    return null;
  }
  //释放链接
  public synchronized static void releaseJedis( final Jedis jedis ) {
    if ( jedis != null ) {
      jedisPool.returnResource( jedis );
    }
  }
}
