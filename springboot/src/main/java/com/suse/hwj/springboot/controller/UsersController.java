package com.suse.hwj.springboot.controller;

import com.github.pagehelper.PageInfo;
import com.suse.hwj.springboot.common.Result;
import com.suse.hwj.springboot.entity.RechargeDTO;
import com.suse.hwj.springboot.entity.Users;
import com.suse.hwj.springboot.service.UsersService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Resource
    private UsersService usersService;

    //新增数据
    @PostMapping("/add")
    public Result add(@RequestBody Users users) {
        usersService.add(users);
        return Result.success();
    }

    //更新数据
    @PutMapping("/update")
    public Result update(@RequestBody Users users) {
        usersService.update(users);
        return Result.success();
    }

    //单个删除数据
    @DeleteMapping("/deleteById/{userId}")
    public Result deleteById(@PathVariable Integer userId) {
        usersService.deleteById(userId);
        return Result.success();
    }

    //批量删除数据
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        usersService.deleteBatch(ids);
        return Result.success();
    }

    //查询所有users的数据
    @GetMapping("/selectAll")
    public Result selectAll(Users users) {
        List<Users> list = usersService.selectAll(users);
        return Result.success(list);
    }

    //分页查询数据
    @GetMapping("/selectPage")
    public Result selectPage(Users users,
                                @RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "5") Integer pageSize) {
        PageInfo<Users> pageInfo = usersService.selectPage(users,pageNum, pageSize);
        return Result.success(pageInfo);
    }

    //查询单个数据
    @GetMapping("/selectById/{userId}")
    public Result selectById(@PathVariable Integer userId) {
        Users dbUser = usersService.selectById(userId);
        if (dbUser == null) {
            return Result.error("500", "未找到该用户");
        }
        return Result.success(dbUser);
    }

    //更新余额
    @PutMapping("/updateByName")
    public Result update(@RequestBody RechargeDTO dto) {
        usersService.updateByName(dto.getAmount(), dto.getName());
        return Result.success();
    }

}
