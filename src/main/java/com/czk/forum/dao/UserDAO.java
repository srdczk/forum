package com.czk.forum.dao;

import com.czk.forum.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
 * created by srdczk 2019/10/30
 */
@Component
@Mapper
public interface UserDAO {
    String INSERT_FILEDS = "username, password, salt, email, activation_code, avatar, gmt_create";
    @Insert("insert into user (" + INSERT_FILEDS + ") values (#{username}, #{password}, #{salt}, #{email}, #{activationCode}, #{avatar}, #{gmtCreate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(User user);

    @Select("select * from user where id=#{id}")
    User getById(@Param(value = "id") Integer id);
    
    @Select("select * from user where username=#{username}")
    User getByName(@Param(value = "username") String username);
    
    @Select("select * from user where email=#{email}")
    User getByEmail(@Param(value = "email") String email);

    @Update("update user set status=1 where id=#{id}")
    int activationSuccess(User user);

    @Update("update user set avatar=#{avatar} where id=#{id}")
    int updateAvatar(@Param(value = "avatar") String avatar, @Param(value = "id") Integer id);

    @Update("update user set salt=#{salt}, password=#{password} where id=#{id}")
    int updatePassword(User user);

}
