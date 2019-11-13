package com.czk.forum;

import com.czk.forum.model.User;
import com.czk.forum.util.RedisUtil;
import org.apache.commons.lang3.SerializationUtils;
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
        User user = new User();
        user.setUsername("老五");
        jedis.set("nima".getBytes(), SerializationUtils.serialize(user));
        user = null;
        System.out.println(user);
        user = SerializationUtils.deserialize(jedis.get("nima".getBytes()));
        System.out.println(user);
        System.out.println(user.getPassword() == null ? "hehe" : "gaga");
    }

}
