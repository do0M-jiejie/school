package com.suse.hwj.springboot.mapper;

import com.suse.hwj.springboot.entity.Users;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    @Update("update `users` set balance = balance + #{amount} where username = #{name}")
    void updateByUsername(@Param("amount") Integer amount, @Param("name") String name);
}
