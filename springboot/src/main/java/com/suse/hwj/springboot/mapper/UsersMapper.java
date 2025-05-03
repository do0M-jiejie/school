package com.suse.hwj.springboot.mapper;

import com.suse.hwj.springboot.entity.Login;
import com.suse.hwj.springboot.entity.Users;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface UsersMapper {

    List<Users> selectAll(@Param("dto") Users users);

    @Select("select * from `users` where user_id = #{userId}")
    Users selectById(Integer userId);

    void insert(Users users);

    void updateById(@Param("dto")Users users);

    @Delete("delete from `users` where user_id = #{userId}")
    void deleteById(Integer userId);

    @Select("select * from `Users` where username = #{username}")
    Users selectByUsername(String username);

    @Update("UPDATE `users` SET balance = balance + #{amount} WHERE username = #{name}")
    int updateByUsername(@Param("amount") BigDecimal amount, @Param("name") String name);

    @Select("select count(*) from `users`")
    int count();

    @Select("select count(*) from `users` where role = #{role}")
    int countByRole(@Param("role") String role);

    @Select("select * from `users` where account = #{account}")
    Users selectByAccount(String account);

    void register(Users users);
}
