package com.czk.forum.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * created by srdczk 2019/11/4
 */
//声明注解在哪儿
@Target(value = ElementType.METHOD)
//声明注解在生效的时间
@Retention(value = RetentionPolicy.RUNTIME)
public @interface LoginRequired {
}
