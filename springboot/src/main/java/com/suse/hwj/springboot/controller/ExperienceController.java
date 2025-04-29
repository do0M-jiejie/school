package com.suse.hwj.springboot.controller;

import com.github.pagehelper.PageInfo;
import com.suse.hwj.springboot.common.Result;
import com.suse.hwj.springboot.entity.Coach;
import com.suse.hwj.springboot.entity.CookBook;
import com.suse.hwj.springboot.entity.Experience;
import com.suse.hwj.springboot.service.ExperienceService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/experience")
public class ExperienceController {

    @Resource
    private ExperienceService experienceService;

    //查询所有experience的数据
    @GetMapping("/selectAll")
    public Result selectAll(Experience experience) {
        List<Experience> list = experienceService.selectAll(experience);
        return Result.success(list);
    }

    //分页查询数据
    @GetMapping("/selectPage")
    public Result selectPage(Experience experience,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize) {
        PageInfo<Experience> pageInfo = experienceService.selectPage(experience,pageNum, pageSize);
        return Result.success(pageInfo);
    }

    @GetMapping("/selectById/{experienceId}")
    public Result selectById(@PathVariable Integer experienceId){
        Experience dbExperience = experienceService.selectById(experienceId);
        if (dbExperience == null) {
            return Result.error("500", "未找到该经验");
        }
        return Result.success(dbExperience);
    }
}
