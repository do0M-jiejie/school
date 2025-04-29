package com.suse.hwj.springboot.controller;

import com.suse.hwj.springboot.common.Result;
import com.suse.hwj.springboot.entity.Equipment;
import com.suse.hwj.springboot.service.EquipmentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    @Resource
    private EquipmentService equipmentService;

    //查询所有equipment的数据
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Equipment> list = equipmentService.selectAll();
        return Result.success(list);
    }
}
