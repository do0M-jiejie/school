package com.suse.hwj.springboot.controller;

import com.github.pagehelper.PageInfo;
import com.suse.hwj.springboot.common.Result;
import com.suse.hwj.springboot.entity.Coach;
import com.suse.hwj.springboot.entity.Users;
import com.suse.hwj.springboot.service.CoachService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coach")
public class CoachController {

    @Resource
    private CoachService coachService;

    //查询所有coach的数据
    @GetMapping("/selectAll")
    public Result selectAll(Coach coach) {
        List<Coach> list = coachService.selectAll(coach);
        return Result.success(list);
    }

    //分页查询数据
    @GetMapping("/selectPage")
    public Result selectPage(Coach coach,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize) {
        PageInfo<Coach> pageInfo = coachService.selectPage(coach,pageNum, pageSize);
        return Result.success(pageInfo);
    }

    @GetMapping("/selectById/{coachId}")
    public Result selectById(@PathVariable Integer coachId){
        Coach dbCoach = coachService.selectById(coachId);
        if (dbCoach == null) {
            return Result.error("500", "未找到该教练");
        }
        return Result.success(dbCoach);
    }

    @GetMapping("/count")
    public Result count() {
        return Result.success(coachService.count());
    }

    //新增数据
    @PostMapping("/add")
    public Result add(@RequestBody Coach coach) {
        coachService.add(coach);
        return Result.success();
    }

    //更新数据
    @PutMapping("/update")
    public Result update(@RequestBody Coach coach) {
        coachService.update(coach);
        return Result.success();
    }

    //单个删除数据
    @DeleteMapping("/deleteById/{coachId}")
    public Result deleteById(@PathVariable Integer coachId) {
        coachService.deleteById(coachId);
        return Result.success();
    }

    //批量删除数据
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        coachService.deleteBatch(ids);
        return Result.success();
    }
}
