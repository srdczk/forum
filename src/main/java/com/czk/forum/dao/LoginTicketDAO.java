package com.czk.forum.dao;

import com.czk.forum.model.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
 * created by srdczk 2019/11/3
 */
@Component
@Mapper
@Deprecated
public interface LoginTicketDAO {
//
//    private Integer userId;
//
//    private String ticket;
//
//    private Integer status;
//
//    private Long expired;
//
    String INSERT_FIELDS = "user_id, ticket, status, expired";

    @Insert("insert into login_ticket (" + INSERT_FIELDS + ") values (#{userId}, #{ticket}, #{status}, #{expired})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(LoginTicket loginTicket);

    @Select("select * from login_ticket where ticket=#{ticket}")
    LoginTicket getByTicket(@Param(value = "ticket") String ticket);

    @Update("update login_ticket set status=#{status} where id=#{id}")
    int updateStatus(LoginTicket loginTicket);
}
