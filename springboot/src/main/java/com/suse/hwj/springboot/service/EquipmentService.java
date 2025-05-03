package com.suse.hwj.springboot.service;

import com.suse.hwj.springboot.entity.Equipment;
import com.suse.hwj.springboot.mapper.EquipmentMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EquipmentService {

    @Resource
    private EquipmentMapper equipmentMapper;

    public List<Equipment> selectAll() {
        return equipmentMapper.selectAll();
    }

    public int count() {
        return equipmentMapper.count();
    }
}
