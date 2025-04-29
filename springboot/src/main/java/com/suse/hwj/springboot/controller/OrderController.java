package com.suse.hwj.springboot.controller;

import com.github.pagehelper.PageInfo;
import com.suse.hwj.springboot.common.Result;
import com.suse.hwj.springboot.entity.CreateOrderDTO;
import com.suse.hwj.springboot.entity.Order;
import com.suse.hwj.springboot.entity.Users;
import com.suse.hwj.springboot.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    private OrderService orderService;

    @PostMapping("/create")
    public Result createOrder(@RequestBody CreateOrderDTO dto) {
        orderService.createOrder(dto.getUserId(), dto.getCourseId());
        return Result.success();
    }

    @GetMapping("/listById/{userId}")
    public Result listById(@PathVariable Integer userId){
        List<Order> dbOrder = orderService.listById(userId);
        return Result.success(dbOrder);
    }

    //分页查询数据
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             @RequestParam Integer userId) {
        PageInfo<Order> pageInfo = orderService.selectPage(pageNum, pageSize,userId);
        return Result.success(pageInfo);
    }
}
