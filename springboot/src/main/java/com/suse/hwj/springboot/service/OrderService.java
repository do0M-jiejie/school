package com.suse.hwj.springboot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.suse.hwj.springboot.entity.Course;
import com.suse.hwj.springboot.entity.Order;
import com.suse.hwj.springboot.mapper.OrderMapper;
import jakarta.annotation.Resource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class OrderService {
    @Resource
    private OrderMapper orderMapper;

    @Resource
    private CourseService courseService;


    public void createOrder(Integer userId, Integer courseId) {
        // 1. 获取课程信息
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }


        // 2. 生成订单编号(年月日时分秒)
        String orderNo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                + ThreadLocalRandom.current().nextInt(10, 99);

        // 检查是否已存在相同订单
        if (orderMapper.selectByOrderNo(orderNo) != null) {
            throw new RuntimeException("订单已存在，请勿重复提交");
        }

        // 3. 创建订单对象
        Order order = new Order();
        order.setOrderNo(orderNo);

        order.setUserId(userId);
        order.setCourseId(courseId);
        order.setCoachId(course.getCoachId());
        order.setCourseName(course.getCourseName());
        order.setCoachName(course.getCoachName());
        order.setPrice(course.getPrice());
        order.setOrderTime(LocalDateTime.now());

        try {
            if (orderMapper.insertOrder(order) != 1) {
                throw new RuntimeException("订单创建失败");
            }
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("请勿重复提交订单");
        }
    }

    public List<Order> listById(Integer userId) {
        return orderMapper.listById(userId);
    }

    public PageInfo<Order> selectPage(Integer pageNum, Integer pageSize, Integer userId) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> list = orderMapper.listById(userId);
        return PageInfo.of(list);
    }
}
