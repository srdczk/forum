package com.czk.forum.dao;

import com.czk.forum.model.Comment;
import com.czk.forum.util.ForumConstant;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * created by srdczk 2019/11/7
 */
@Component
@Mapper
public interface CommentDAO {

    @Select("select * from comment where status=0 and entity_type=#{entityType} and entity_id=#{entityId} order by gmt_create desc limit #{off}, #{cnt}")
    List<Comment> selectCommentByEntity(@Param(value = "entityType") Integer entityType, @Param(value = "entityId") Integer entityId, @Param(value = "off") Integer off, @Param(value = "cnt") Integer cnt);

    @Select("select count(*) from comment where status=0 and entity_type=#{entityType} and entity_id=#{entityId}")
    Integer countCommentByEntity(@Param(value = "entityType") Integer entityType, @Param(value = "entityId") Integer entityId);

    @Insert("insert into comment (user_id, entity_type, entity_id, target_id, content, gmt_create) values(#{userId}, #{entityType}, #{entityId}, #{targetId}, #{content}, #{gmtCreate})")
    void add(Comment comment);

    @Select("select * from comment where id=#{id}")
    Comment getById(@Param(value = "id") Integer id);

}
