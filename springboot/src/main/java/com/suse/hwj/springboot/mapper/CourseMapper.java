package com.suse.hwj.springboot.mapper;

import com.suse.hwj.springboot.entity.Course;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseMapper {
    List<Course> selectAll();

    @Select("select * from `course` where course_id = #{courseId} limit 0,4")
    Course selectById(Integer courseId);

    @Select("select * from `course` where course_id = #{courseId}")
    Course getCourseById(Integer courseId);

    @Select("select * from `course` where coach_id = #{coachId}")
    List<Course> selectByCoachId(Integer coachId);

    @Select("select count(*) from `course`")
    int count();

    void insert(Course course);

    void updateById(@Param("dto")Course course);
}
