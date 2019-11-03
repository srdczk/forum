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
    long DEFAULT_MILSECONDS = 1000 * 3600 * 12;

    long REMBER_MILSECONDS = 1000 * 3600 * 24 * 100;

}
