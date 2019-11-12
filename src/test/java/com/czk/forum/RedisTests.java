package com.czk.forum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ForumApplication.class)
public class RedisTests {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Test
    public void testRedis() {
        String redisKey = "test:count";
        redisTemplate.opsForValue().set(redisKey, 1);
        System.out.println(redisTemplate.opsForValue().get(redisKey));
        redisTemplate.opsForValue().increment(redisKey);
        System.out.println(redisTemplate.opsForValue().get(redisKey));
        String key = "test:user";
        redisTemplate.opsForHash().put(key, "name", "司马");
        System.out.println(redisTemplate.opsForHash().get(key, "name"));

    }

    @Test
    public void testLists() {
        String listKey = "test:list";
        redisTemplate.opsForList().leftPush(listKey, "101");
        redisTemplate.opsForList().leftPush(listKey, "jkds");
        System.out.println(redisTemplate.opsForList().size(listKey));
        System.out.println(redisTemplate.opsForList().index(listKey, 0));
        List list = redisTemplate.opsForList().range(listKey, 0, 2);
        System.out.println(list);
    }
    @Test
    public void testSets() {
        String redisKey = "test:set";
        redisTemplate.opsForSet().add(redisKey, "刘备", "上帝", "老五");
        System.out.println(redisTemplate.opsForSet().size(redisKey));
        System.out.println(redisTemplate.opsForSet().pop(redisKey));
    }

    @Test
    public void testZSet() {
        String key = "test:zset";
        redisTemplate.opsForZSet().add(key, "shacengs", 23);
    }

}
