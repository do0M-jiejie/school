package com.suse.hwj.springboot.mapper;

import com.suse.hwj.springboot.entity.Coach;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CoachMapper {

    List<Coach> selectAll(@Param("dto") Coach coach);

    @Select("select * from `coach` where coach_id = #{coachId}")
    Coach selectById(Integer coachId);

    @Select("select count(*) from `coach`")
    int count();

    @Select("select * from `coach` where account = #{account}")
    Coach selectByAccount(String account);

    void insert(Coach coach);

    void updateById(@Param("dto")Coach coach);

    @Delete("delete from `coach` where coach_id = #{coachId}")
    void deleteById(Integer coachId);
}
