package com.suse.hwj.springboot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.suse.hwj.springboot.entity.Coach;
import com.suse.hwj.springboot.exception.CustomException;
import com.suse.hwj.springboot.mapper.CoachMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CoachService {

    @Resource
    private CoachMapper coachMapper;

    public List<Coach> selectAll(Coach coach) {
        return coachMapper.selectAll(coach);
    }

    public Coach selectById(Integer coachId) {
        return coachMapper.selectById(coachId);
    }

    public PageInfo<Coach> selectPage(Coach coach, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Coach> list = coachMapper.selectAll(coach);
        return PageInfo.of(list);
    }

    public Coach login(Coach coach) {
        // 1. 参数校验
        if (coach == null || coach.getAccount() == null || coach.getPassword() == null) {
            throw new CustomException("500", "参数不完整");
        }

        // 2. 用户名长度校验
        if (coach.getAccount().length() < 3 || coach.getAccount().length() > 20) {
            throw new CustomException("500", "用户名长度必须在3-20个字符之间");
        }

        // 3. 密码长度校验
        if (coach.getPassword().length() < 3 || coach.getPassword().length() > 20) {
            throw new CustomException("500", "密码长度必须在3-20个字符之间");
        }

        // 4. 查询教练
        Coach dbCoach = coachMapper.selectByAccount(coach.getAccount());
        if (dbCoach == null) {
            throw new CustomException("500", "教练账号不存在");
        }

        // 5. 检查教练状态
        if ("封禁".equals(dbCoach.getStatus())) {
            throw new CustomException("500", "账号已被禁用");
        }

        // 6. 密码校验
        if (!dbCoach.getPassword().equals(coach.getPassword())) {
            throw new CustomException("500", "密码错误");
        }

        return dbCoach;
    }

    public int count() {
        return coachMapper.count();
    }

    public void add(Coach coach) {
        coachMapper.insert(coach);
    }

    public void update(Coach coach){
        coachMapper.updateById(coach);
    }

    public void deleteById(Integer coachId) {
        coachMapper.deleteById(coachId);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer userId : ids) {
            this.deleteById(userId);
        }
    }
}
