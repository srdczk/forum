package com.czk.forum.util;

public class RedisUtil {

    private static final String SPLIT = ":";

    private static final String PREFIX_ENTITY_LIKE = "like:entity";

    private static final String PREFIX_USER_LIKE = "like:user";

    private static final String PREFIX_FOLLOWEE = "followee";

    private static final String PREFIX_FOLLOWER = "follower";

    //生成某个实体的赞
    // 需要传入实体相关
    // like:enity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType, int entityId) {
        // 点赞和取消
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }
    // 开发业务组件

    // 获得每一个用户的赞数
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    // 某个用户关注的实体
    // 谁关注的, 关注那个实体
    // followee:userId:entityType -> zset(entityId, now)
    public static String getFolloweeKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }
    // 某个用户拥有的粉丝儿
    // 这个是一个什么东西, 给其定位一下, 啥东西都能够有粉丝儿
    // follower:entityType:entityId -> zset(userId, now)
    public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

}
