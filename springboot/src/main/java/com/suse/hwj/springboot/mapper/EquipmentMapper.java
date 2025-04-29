package com.suse.hwj.springboot.mapper;

import com.suse.hwj.springboot.entity.Equipment;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EquipmentMapper {

    @Select("select * from equipment")
    List<Equipment> selectAll();
}
