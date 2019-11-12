package com.czk.forum.service;

import com.czk.forum.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    // 点赞
    public void like(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisUtil.getEntityLikeKey(entityType, entityId);
//        System.out.println(entityLikeKey);
        if (redisTemplate.opsForSet().isMember(entityLikeKey, userId)) {
            redisTemplate.opsForSet().remove(entityLikeKey, userId);
        } else {
            redisTemplate.opsForSet().add(entityLikeKey, userId);
        }
    }
    // 查询一个实体点赞的数量
    public long findEntityCount(int entityType, int entityId) {
        String entityKey = RedisUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityKey);
    }

    // 查询某人对某实体是否点过赞
    public int findStatus(int userId, int entityType, int entityId) {
        String entityKey = RedisUtil.getEntityLikeKey(entityType, entityId);

        return redisTemplate.opsForSet().isMember(entityKey, userId) ? 1 : 0;
    }
}
