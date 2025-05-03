package com.suse.hwj.springboot.controller;

import com.github.pagehelper.PageInfo;
import com.suse.hwj.springboot.common.Result;
import com.suse.hwj.springboot.entity.Course;
import com.suse.hwj.springboot.entity.Users;
import com.suse.hwj.springboot.service.CourseService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

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

    //分页查询数据
    @GetMapping("/selectPage")
    public Result selectPage(Course course,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize) {
        PageInfo<Course> pageInfo = courseService.selectPage(course,pageNum, pageSize);
        return Result.success(pageInfo);
    }

    @GetMapping("/selectById/{courseId}")
    public Result selectById(@PathVariable Integer courseId){
        Course dbCourse = courseService.selectById(courseId);
        if (dbCourse == null) {
            return Result.error("500", "未找到该课程");
        }
        return Result.success(dbCourse);
    }

    @GetMapping("/selectByCoachId/{coachId}")
    public Result selectByCoachId(@PathVariable Integer coachId){
        List<Course> list = courseService.selectByCoachId(coachId);
        return Result.success(list);
    }


    @GetMapping("/count")
    public Result count() {
        return Result.success(courseService.count());
    }

    //新增数据
    @PostMapping("/add")
    public Result add(@RequestBody Course course) {
        courseService.add(course);
        return Result.success();
    }

    //更新数据
    @PutMapping("/update")
    public Result update(@RequestBody Course course) {
        courseService.update(course);
        return Result.success();
    }
}
