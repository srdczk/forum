package com.czk.forum.service;

import com.czk.forum.dao.UserDAO;
import com.czk.forum.util.HostHolder;
import com.czk.forum.util.RedisAdapter;
import com.czk.forum.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FollowService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RedisAdapter adapter;

    @Autowired
    private HostHolder hostHolder;

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

    public List<Map<String, Object>> findFollowers(int userId, int off, int cnt) {
        List<Map<String, Object>> res = new ArrayList<>();
        String key = RedisUtil.getFollowerKey(1, userId);

        Set<String> set = adapter.getPage(key, off, cnt);
        for (String s : set) {
            Map<String, Object> map = new HashMap<>();
            map.put("user", userDAO.getById(Integer.parseInt(s)));
            // 还需要传入 : 时间
            map.put("date", (long) adapter.zscore(key, s));
            if (hostHolder.getUser() != null && hasFollowed(hostHolder.getUser().getId(), 1, Integer.parseInt(s))) {
                map.put("followed", true);
            } else map.put("followed", false);
            res.add(map);
        }
        return res;
    }

    public List<Map<String, Object>> findFollowees(int userId, int off, int cnt) {
        List<Map<String, Object>> res = new ArrayList<>();
        String key = RedisUtil.getFolloweeKey(userId, 1);
        // 查到所有的用户
        Set<String> set = adapter.getPage(key, off, cnt);
        for (String s : set) {
            Map<String, Object> map = new HashMap<>();
            map.put("user", userDAO.getById(Integer.parseInt(s)));
            map.put("date", (long) adapter.zscore(key, s));
            if (hostHolder.getUser() != null && hasFollowed(hostHolder.getUser().getId(), 1, Integer.parseInt(s))) {
                map.put("followed", true);
            } else map.put("followed", false);
            res.add(map);
        }
        return res;
    }

}
