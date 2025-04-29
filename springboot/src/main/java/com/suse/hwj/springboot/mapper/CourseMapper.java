package com.suse.hwj.springboot.mapper;

import com.suse.hwj.springboot.entity.Course;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CourseMapper {
    List<Course> selectAll();

    @Select("select * from `course` where course_id = #{courseId} limit 0,4")
    Course selectById(Integer courseId);

    @Select("select * from `course` where course_id = #{courseId}")
    Course getCourseById(Integer courseId);
}
