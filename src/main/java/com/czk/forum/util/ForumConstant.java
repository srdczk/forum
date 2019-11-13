package com.czk.forum.util;

/**
 * created by srdczk 2019/11/3
 */
public interface ForumConstant {
    //激活状态
    int ACTIVATION_SUCCESS = 0;

    int ACTIVATION_REPEAT = 1;

    int ACTIVATION_FAILUE = 2;

    //默认状态下的超时时间
    int DEFAULT_SECONDS = 3600 * 12;

    int REMBER_SECONDS = 3600 * 24 * 100;

    //帖子的实体类型
    int ENTITY_TYPE_POST = 1;

    int ENTITY_TYPE_COMMENT = 2;

    /**
     * topics: 主题
     */
    // 评论
    String TOPIC_COMMENT = "comment";
    // 点赞
    String TOPIC_LIKE = "like";
    // 关注
    String TOPIC_FOLLOW = "follow";
    // 系统用户的id
    int SYSTEM_USER_ID = 1;
}
