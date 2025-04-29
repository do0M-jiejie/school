package com.suse.hwj.springboot.mapper;

import com.suse.hwj.springboot.entity.Coach;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CoachMapper {

    List<Coach> selectAll();

    @Select("select * from `coach` where coach_id = #{coachId}")
    Coach selectById(Integer coachId);
}
