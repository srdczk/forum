package com.czk.forum.service;

import com.czk.forum.util.RedisAdapter;
import com.czk.forum.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private RedisAdapter adapter;

    // 统计某一个用户被赞的数量
    public int findUserLikeCount(int userId) {
        String userKey = RedisUtil.getUserLikeKey(userId);
        String res = adapter.get(userKey);
        if (res == null) return 0;
        else return Integer.parseInt(res);
    }

    // 点赞
    public void like(int userId, int entityType, int entityId, int entityUserId) {

        adapter.like(userId, entityType, entityId, entityUserId);

    }
    // 查询一个实体点赞的数量
    public long findEntityCount(int entityType, int entityId) {
        String entityKey = RedisUtil.getEntityLikeKey(entityType, entityId);
        return adapter.scard(entityKey);
    }

    // 查询某人对某实体是否点过赞
    public int findStatus(int userId, int entityType, int entityId) {
        String user = String.valueOf(userId);
        String entityKey = RedisUtil.getEntityLikeKey(entityType, entityId);
        return adapter.sismember(entityKey, user) ? 1 : 0;
    }
}
