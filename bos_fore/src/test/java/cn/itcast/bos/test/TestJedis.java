package cn.itcast.bos.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class TestJedis {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testJedis() {
        Jedis jedis = new Jedis("192.168.25.128", 6379);
        jedis.set("name", "xuxu");
        String name = jedis.get("name");
        System.out.println(name);
    }

    @Test
    public void testJedisTemplate() {
        redisTemplate.opsForValue().set("username", "jack", 10, TimeUnit.SECONDS);
        String name = (String) redisTemplate.opsForValue().get("username");
        System.out.println(name);
    }
}
