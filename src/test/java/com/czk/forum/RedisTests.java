package com.czk.forum;

import com.czk.forum.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ForumApplication.class)
public class RedisTests {

    @Test
    public void testJedis() {
        Jedis jedis = new Jedis();
        jedis.zadd("nima", 23, "sima");
        Set<String> set = jedis.zrange("nima",0, 2);
        for (String s : set) {
            System.out.println(s);
        }

    }

}
