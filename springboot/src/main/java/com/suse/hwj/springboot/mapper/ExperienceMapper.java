package com.suse.hwj.springboot.mapper;

import com.suse.hwj.springboot.entity.CookBook;
import com.suse.hwj.springboot.entity.Experience;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ExperienceMapper {

    List<Experience> selectAll(@Param("dto") Experience experience);

    @Select("select * from `experience` where experience_id = #{experienceId}")
    Experience selectById(Integer experienceId);
}
