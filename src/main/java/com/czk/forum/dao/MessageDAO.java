package com.czk.forum.dao;

import com.czk.forum.model.Message;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * created by srdczk 2019/11/11
 */
@Component
@Mapper
public interface MessageDAO {

    String INSERT_FIELDS = "from_id, to_id, conversation_id, content, gmt_create";

    //插入数据
    @Insert("insert into message (" + INSERT_FIELDS + ") values (#{fromId}, #{toId}, #{conversationId}, #{content}, #{gmtCreate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(Message message);

    // 查询当前用户的会话列表
    // 针对每个会话只返回一条最新的私信
    @Select("select * from message where id in (select max(id) from message where status != 2 and from_id != 1 and (from_id=#{userId} or to_id=#{userId}) group by conversation_id) order by id desc limit #{off}, #{cnt}")
    List<Message> selectConversation(@Param(value = "userId") Integer userId, @Param(value = "off") Integer off, @Param(value = "cnt") Integer cnt);

    // 查询当前用户的会话数量
    @Select("select count(m.maxid) from (select max(id) as maxid from message where status != 2 and from_id != 1 and (from_id=#{userId} or to_id=#{userId}) group by conversation_id) as m")
    Integer selectConversationCount(@Param(value = "userId") Integer userId);

    //查询某一个会话的私信列表
    @Select("select * from message where status != 2 and from_id != 1 and conversation_id=#{conversationId} order by id desc limit #{off}, #{cnt}")
    List<Message> selectLetters(@Param(value = "conversationId") String conversationId, @Param(value = "off") Integer off, @Param(value = "cnt") Integer cnt);

    //查询数量
    @Select("select count(*) from message where status != 2 and from_id != 1 and conversation_id=#{conversationId}")
    Integer selectLetterCount(@Param(value = "conversationId") String conversationId);

    //查询未读的私信的数量
    @Select("select count(*) from message where status=0 and from_id != 1 and to_id=#{userId}")
    Integer selectUnreadCount(@Param(value = "userId") Integer userId);

    @Select("select count(*) from message where status=0 and from_id != 1 and to_id=#{userId} and conversation_id=#{conversationId}")
    Integer selectConversationUnread(@Param(value = "userId") Integer userId, @Param(value = "conversationId") String conversationId);

    //修改
    @Update("update message set status=#{status} where id=#{id}")
    int updateStatus(@Param(value = "id") Integer id, @Param(value = "status") Integer status);

}
