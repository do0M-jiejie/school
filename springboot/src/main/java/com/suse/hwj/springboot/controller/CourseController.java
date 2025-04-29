package com.suse.hwj.springboot.controller;

import com.suse.hwj.springboot.common.Result;
import com.suse.hwj.springboot.entity.Course;
import com.suse.hwj.springboot.service.CourseService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Resource
    private CourseService courseService;

    //查询所有course的数据
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Course> list = courseService.selectAll();
        return Result.success(list);
    }

    @GetMapping("/selectById/{courseId}")
    public Result selectById(@PathVariable Integer courseId){
        Course dbCourse = courseService.selectById(courseId);
        if (dbCourse == null) {
            return Result.error("500", "未找到该课程");
        }
        return Result.success(dbCourse);
    }
}
