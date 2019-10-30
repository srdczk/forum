package com.czk.forum.dao;

import com.czk.forum.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * created by srdczk 2019/10/30
 */
@Component
@Mapper
public interface UserDAO {
    String INSERT_FILEDS = "username, password, salt, email, activation_code, avatar, gmt_create";
    @Insert("insert into user (" + INSERT_FILEDS + ") values (#{username}, #{password}, #{salt}, #{email}, #{activationCode}, #{avatar}, #{gmtCreate})")
    void add(User user);

    @Select("select * from user where id=#{id}")
    User getById(@Param(value = "id") Integer id);
}
