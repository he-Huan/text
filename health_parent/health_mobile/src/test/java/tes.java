import com.itheima.constant.RedisMessageConstant;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class tes {
    @Test
    public void te(){
        JedisPool jedisPool = new JedisPool();
        Jedis jedis = jedisPool.getResource();
        String key = "order_"+ RedisMessageConstant.SENDTYPE_ORDER + "_18075854910";
        jedis.setex(key,5*60,"111111");
    }


    @Test
    public void login(){
        JedisPool jedisPool = new JedisPool();
        Jedis jedis = jedisPool.getResource();
        String key = "login_"+ RedisMessageConstant.SENDTYPE_LOGIN + "_13319569021";
        jedis.setex(key,5*60,"1111");
    }

}
