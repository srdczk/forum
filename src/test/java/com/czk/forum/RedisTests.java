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

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ForumApplication.class)
public class RedisTests {
    @Autowired
    private Jedis jedis;

    @Test
    public void testJedis() {
        jedis.set("nima", "34");
        System.out.println(jedis.get("nima"));
        String c = jedis.get("sd");
        if (c == null) System.out.println("NAMSILE");
        else System.out.println(c);
    }

}
