package com.suse.hwj.springboot.controller;

import com.suse.hwj.springboot.common.Result;
import com.suse.hwj.springboot.entity.Admin;
import com.suse.hwj.springboot.entity.Login;
import com.suse.hwj.springboot.entity.Users;
import com.suse.hwj.springboot.entity.Coach;
import com.suse.hwj.springboot.exception.CustomException;
import com.suse.hwj.springboot.service.AdminService;
import com.suse.hwj.springboot.service.CoachService;
import com.suse.hwj.springboot.service.UsersService;
import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    private static final String ROLE_USER = "普通用户";
    private static final String ROLE_COACH = "教练";
    private static final String ROLE_ADMIN = "管理员";

    @Resource
    private UsersService usersService;
    @Resource
    private CoachService coachService;
    @Resource
    private AdminService adminService;

    @PostMapping("/login")
    public Result login(@RequestBody Login login) {
        // 参数验证
        if (login == null) {
            return Result.error("500", "登录参数不能为空");
        }
        if (!StringUtils.hasText(login.getAccount()) || !StringUtils.hasText(login.getPassword())) {
            return Result.error("400", "账号和密码不能为空");
        }
        if (!StringUtils.hasText(login.getRole())) {
            return Result.error("400", "请选择登录角色");
        }

        // 根据角色进行不同的登录处理
        switch (login.getRole()) {
            case ROLE_USER:
                Users userParam = new Users();
                userParam.setAccount(login.getAccount());
                userParam.setPassword(login.getPassword());
                userParam.setRole(ROLE_USER);
                Users dbUsers = usersService.login(userParam);
                return Result.success(dbUsers);
            case ROLE_COACH:
                Coach coachParam = new Coach();
                coachParam.setAccount(login.getAccount());
                coachParam.setPassword(login.getPassword());
                Coach dbCoach = coachService.login(coachParam);
                return Result.success(dbCoach);
            case ROLE_ADMIN:
                Admin adminParam = new Admin();
                adminParam.setAccount(login.getAccount());
                adminParam.setPassword(login.getPassword());
                Admin dbAdmin = adminService.login(adminParam);
                return Result.success(dbAdmin);
            default:
                return Result.error("400", "无效的角色类型");
        }

    }

    @PostMapping("/register")
    public Result register(@RequestBody Users users){
        usersService.register(users);
        return Result.success();
    }
}
