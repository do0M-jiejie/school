package com.suse.hwj.springboot.controller;

import com.suse.hwj.springboot.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;



}
