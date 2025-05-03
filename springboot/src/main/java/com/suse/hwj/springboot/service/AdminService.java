package com.suse.hwj.springboot.service;

import com.suse.hwj.springboot.entity.Admin;
import com.suse.hwj.springboot.exception.CustomException;
import com.suse.hwj.springboot.mapper.AdminMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminService {

    @Resource
    private AdminMapper adminMapper;

    public Admin login(Admin admin) {
        // 1. 参数校验
        if (admin == null || admin.getAccount() == null || admin.getPassword() == null) {
            throw new CustomException("500", "参数不完整");
        }

        // 2. 用户名长度校验
        if (admin.getAccount().length() < 3 || admin.getAccount().length() > 20) {
            throw new CustomException("500", "用户名长度必须在3-20个字符之间");
        }

        // 3. 密码长度校验
        if (admin.getPassword().length() < 3 || admin.getPassword().length() > 20) {
            throw new CustomException("500", "密码长度必须在6-20个字符之间");
        }

        // 4. 查询管理员
        Admin dbAdmin = adminMapper.selectByAccount(admin.getAccount());
        if (dbAdmin == null) {
            throw new CustomException("500", "教练账号不存在");
        }

        // 6. 密码校验
        if (!dbAdmin.getPassword().equals(admin.getPassword())) {
            throw new CustomException("500", "密码错误");
        }

        return dbAdmin;
    }
}
