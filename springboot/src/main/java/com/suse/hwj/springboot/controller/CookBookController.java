package com.suse.hwj.springboot.controller;

import com.github.pagehelper.PageInfo;
import com.suse.hwj.springboot.common.Result;
import com.suse.hwj.springboot.entity.CookBook;
import com.suse.hwj.springboot.service.CookBookService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cookbook")
public class CookBookController {

    @Resource
    private CookBookService cookBookService;

    //查询所有cookbook的数据
    @GetMapping("/selectAll")
    public Result selectAll(CookBook cookBook) {
        List<CookBook> list = cookBookService.selectAll(cookBook);
        return Result.success(list);
    }

    //分页查询数据
    @GetMapping("/selectPage")
    public Result selectPage(CookBook cookBook,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize) {
        PageInfo<CookBook> pageInfo = cookBookService.selectPage(cookBook,pageNum, pageSize);
        return Result.success(pageInfo);
    }
}
