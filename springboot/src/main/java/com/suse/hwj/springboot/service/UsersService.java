package com.suse.hwj.springboot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.suse.hwj.springboot.entity.Users;
import com.suse.hwj.springboot.exception.CustomException;
import com.suse.hwj.springboot.mapper.UsersMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class UsersService {

    @Resource
    private UsersMapper usersMapper;

    public void add(Users users) {
        usersMapper.insert(users);
    }

    public void update(Users users) {
        usersMapper.updateById(users);
    }

    public void deleteById(Integer userId) {
        usersMapper.deleteById(userId);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer userId : ids) {
            this.deleteById(userId);
        }
    }

    public Users selectById(Integer userId) {
        return usersMapper.selectById(userId);
    }

    public List<Users> selectAll(Users users) {
        return usersMapper.selectAll(users);
    }

    public PageInfo<Users> selectPage(Users users, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Users> list = usersMapper.selectAll(users);
        return PageInfo.of(list);
    }

    public Users login(Users users) {
        // 1. 参数校验
        if (users == null || users.getAccount() == null || users.getPassword() == null || users.getRole() == null) {
            throw new CustomException("500", "参数不完整");
        }

        // 2. 用户名长度校验
        if (users.getAccount().length() < 3 || users.getAccount().length() > 20) {
            throw new CustomException("500", "用户名长度必须在3-20个字符之间");
        }

        // 3. 密码长度校验
        if (users.getPassword().length() < 3 || users.getPassword().length() > 20) {
            throw new CustomException("500", "密码长度必须在6-20个字符之间");
        }

        // 4. 角色校验
        if (!"管理员".equals(users.getRole()) && !"普通用户".equals(users.getRole()) && !"教练".equals(users.getRole())) {
            throw new CustomException("500", "无效的角色类型");
        }

        // 5. 查询用户
        Users dbUsers = usersMapper.selectByAccount(users.getAccount());
        if (dbUsers == null) {
            throw new CustomException("500", "用户不存在");
        }

        // 6. 检查用户状态
        if ("封禁".equals(dbUsers.getStatus())) {
            throw new CustomException("500", "账号已被禁用");
        }

        // 7. 密码校验
        if (!dbUsers.getPassword().equals(users.getPassword())) {
            throw new CustomException("500", "密码错误");
        }

        // 8. 角色校验
        if (!dbUsers.getRole().equals(users.getRole())) {
            throw new CustomException("500", "角色不匹配");
        }

        return dbUsers;
    }

    public void updateByName(BigDecimal amount, String name) {
        // 验证金额有效性
        if (amount == null) {
            throw new IllegalArgumentException("充值金额必须大于0");
        }

        // 执行更新
        int affectedRows = usersMapper.updateByUsername(amount, name.trim());

        // 验证更新结果
        if (affectedRows == 0) {
            throw new RuntimeException("用户不存在或更新失败");
        }
    }

    public int count() {
        return usersMapper.count();
    }

    public void register(Users user) {
        usersMapper.register(user);
    }
}
