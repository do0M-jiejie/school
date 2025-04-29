package com.suse.hwj.springboot.mapper;

import com.suse.hwj.springboot.entity.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrderMapper {

    int insertOrder(Order order);

    @Select("SELECT * FROM `order` WHERE order_no = #{orderNo}")
    Order selectByOrderNo(String orderNo);

    @Select("select * from `order` where user_id = #{userId}")
    List<Order> listById(@Param("userId")Integer userId);
}
