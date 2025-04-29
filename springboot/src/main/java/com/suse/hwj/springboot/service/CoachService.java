package com.suse.hwj.springboot.service;

import com.suse.hwj.springboot.entity.Coach;
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

    public List<Coach> selectAll() {
        return coachMapper.selectAll();
    }

    public Coach selectById(Integer coachId) {
        return coachMapper.selectById(coachId);
    }
}
