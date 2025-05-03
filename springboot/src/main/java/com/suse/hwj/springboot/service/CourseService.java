package com.suse.hwj.springboot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
    }//

    public Course selectById(Integer courseId) {
        return courseMapper.selectById(courseId);
    }

    public Course getCourseById(Integer courseId) {
        return courseMapper.getCourseById(courseId);
    }

    public List<Course> selectByCoachId(Integer coachId) {
        return courseMapper.selectByCoachId(coachId);
    }


    public int count() {
        return courseMapper.count();
    }

    public PageInfo<Course> selectPage(Course course, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Course> list = courseMapper.selectAll();
        return PageInfo.of(list);
    }//

    public void add(Course course) {
        courseMapper.insert(course);
    }

    public void update(Course course) {
        courseMapper.updateById(course);
    }

    public void deleteById(Integer courseId) {
        courseMapper.deleteById(courseId);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer courseId : ids) {
            this.deleteById(courseId);
        }
    }
}
