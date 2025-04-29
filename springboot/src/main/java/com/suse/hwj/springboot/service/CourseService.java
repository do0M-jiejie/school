package com.suse.hwj.springboot.service;

import com.suse.hwj.springboot.entity.Course;
import com.suse.hwj.springboot.mapper.CourseMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CourseService {

    @Resource
    private CourseMapper courseMapper;

    public List<Course> selectAll() {
        return courseMapper.selectAll();
    }

    public Course selectById(Integer courseId) {
        return courseMapper.selectById(courseId);
    }

    public Course getCourseById(Integer courseId) {
        return courseMapper.getCourseById(courseId);
    }
}
