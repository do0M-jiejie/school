package com.suse.hwj.springboot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.suse.hwj.springboot.entity.Users;
import com.suse.hwj.springboot.exception.CustomException;
import com.suse.hwj.springboot.mapper.UsersMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        String username = users.getUsername();
        Users dbUsers = usersMapper.selectByUsername(username);
        if (dbUsers == null) {
            throw new CustomException("500", "用户不存在");
        }
        String password =  users.getPassword();
        if (!dbUsers.getPassword().equals(password)) {
            throw new CustomException("500", "账号或密码错误");
        }
        String role =  users.getRole();
        if (!dbUsers.getRole().equals(role)) {
            throw new CustomException("500", "身份有误");
        }
        return dbUsers;
    }

    public void updateByName(Integer amount, String name) {
        usersMapper.updateByUsername(amount,name);
    }
}
