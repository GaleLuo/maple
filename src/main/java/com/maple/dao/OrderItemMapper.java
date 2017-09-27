package com.maple.dao;

import com.maple.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    void batchInsert(List<OrderItem> orderItemList);

    List<OrderItem> selectByOrderNum(Long orderNo);

    List<OrderItem> selectByOrderNumAndUserId(@Param("orderNo") Long orderNo, @Param("userId") Integer userId);
}