package com.czk.forum.util;

public class RedisUtil {

    private static final String SPLIT = ":";

    private static final String PREFIX_ENTITY_LIKE = "like:entity";

    //生成某个实体的赞
    // 需要传入实体相关
    // like:enity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType, int entityId) {
        // 点赞和取消
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }
    // 开发业务组件

}
