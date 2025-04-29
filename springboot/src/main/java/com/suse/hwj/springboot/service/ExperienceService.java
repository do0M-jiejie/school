package com.suse.hwj.springboot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.suse.hwj.springboot.entity.Experience;
import com.suse.hwj.springboot.mapper.ExperienceMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ExperienceService {

    @Resource
    private ExperienceMapper experienceMapper;

    public List<Experience> selectAll(Experience experience) {
        return experienceMapper.selectAll(experience);
    }

    public PageInfo<Experience> selectPage(Experience experience, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Experience> list = experienceMapper.selectAll(experience);
        return PageInfo.of(list);
    }

    public Experience selectById(Integer experienceId) {
        return experienceMapper.selectById(experienceId);
    }
}
