package com.suse.hwj.springboot.controller;

import com.suse.hwj.springboot.common.Result;
import com.suse.hwj.springboot.entity.Coach;
import com.suse.hwj.springboot.service.CoachService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/coach")
public class CoachController {

    @Resource
    private CoachService coachService;

    //查询所有coach的数据
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Coach> list = coachService.selectAll();
        return Result.success(list);
    }

    @GetMapping("/selectById/{coachId}")
    public Result selectById(@PathVariable Integer coachId){
        Coach dbCoach = coachService.selectById(coachId);
        if (dbCoach == null) {
            return Result.error("500", "未找到该教练");
        }
        return Result.success(dbCoach);
    }
}
