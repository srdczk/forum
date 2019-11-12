package com.czk.forum.service;

import com.czk.forum.util.RedisAdapter;
import com.czk.forum.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowService {
    @Autowired
    private RedisAdapter adapter;

    public void follow(int userId, int entityType, int entityId) {
        // 一项业务, 两项事
        adapter.follow(userId, entityType, entityId);
    }

    public void unfollow(int userId, int entityType, int entityId) {
        // 一项业务, 两项事
        adapter.unfollow(userId, entityType, entityId);
    }

    public boolean isMember(int userId, int entityType, int entityId) {
        String followerKey = RedisUtil.getFollowerKey(entityType, entityId);
        return adapter.sismember(followerKey, String.valueOf(userId));
    }

    // 查询有多少人关注
    // follower:entityType:entityId -> userId
    public long followerCount(int userId) {
        String followerKey = RedisUtil.getFollowerKey(1, userId);
        return adapter.zcard(followerKey);
    }
    // 查询该用户关注了多少人
    // followee:userId:entityType -> entityId
    public long followeeCount(int userId) {
        String followeeKey = RedisUtil.getFolloweeKey(userId, 1);
        return adapter.zcard(followeeKey);
    }

    public boolean hasFollowed(int userId, int entityType, int entityId) {

        String followerKey = RedisUtil.getFollowerKey(entityType, entityId);
        return adapter.isMember(followerKey, String.valueOf(userId));

    }

}
