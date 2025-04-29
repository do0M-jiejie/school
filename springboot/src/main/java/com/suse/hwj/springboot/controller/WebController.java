package com.suse.hwj.springboot.controller;

import com.suse.hwj.springboot.common.Result;
import com.suse.hwj.springboot.entity.Users;
import com.suse.hwj.springboot.exception.CustomException;
import com.suse.hwj.springboot.service.UsersService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Objects;

@RestController
public class WebController {

    @Resource
    private UsersService usersService;

    @PostMapping("/login")
    public Result login(@RequestBody Users users){
        Users dbUsers = usersService.login(users);
        return Result.success(dbUsers);
    }
}
