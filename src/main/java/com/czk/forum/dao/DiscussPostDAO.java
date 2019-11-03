package com.czk.forum.dao;

import com.czk.forum.model.DiscussPost;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * created by srdczk 2019/10/30
 */
@Component
@Mapper
public interface DiscussPostDAO {
    String INSERT_FIELDS = "user_id, title, content, gmt_create";

    @Insert("insert into discuss_post (" + INSERT_FIELDS + ") values (#{userId}, #{title}, #{content}, #{gmtCreate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(DiscussPost discussPost);

    @Select("select * from discuss_post order by type desc, gmt_create desc limit #{off}, #{cnt}")
    List<DiscussPost> getByPage(@Param(value = "off") Integer off, @Param(value = "cnt") Integer cnt);

    @Select("select * from discuss_post where user_id = #{userId} order by type desc, gmt_create desc limit #{off}, #{cnt}")
    List<DiscussPost> getByUserIdAndPage(@Param(value = "userId") Integer userId, @Param(value = "off") Integer off, @Param(value = "cnt") Integer cnt);

    @Select("select count(*) from discuss_post")
    Integer sum();

    @Select("select count(*) from discuss_post where user_id = #{userId}")
    Integer sumOfUser(@Param(value = "userId") Integer userId);
}
