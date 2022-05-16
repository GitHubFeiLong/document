import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * 类描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2022/5/16 15:04
 */

public class Test1 {
    //~fields
    //==================================================================================================================

    //~construct methods
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @Test
    public void testConnectSentinel() {
        Set<String> sentinels = new HashSet<>();
        sentinels.add("10.168.60.100:26379");
        sentinels.add("10.168.60.100:26380");
        sentinels.add("10.168.60.100:26381");
        String clusterName = "mymaster";
        JedisSentinelPool redisSentinelPool = new JedisSentinelPool(clusterName, sentinels);
        Jedis jedis = null;
        try {
            jedis = redisSentinelPool.getResource();
            jedis.set("name01", "aaa");
            System.out.println(jedis.get("name01"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
            redisSentinelPool.close();
        }
    }
}
