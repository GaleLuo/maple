package com.maple.dao;

import com.maple.jo.FinishOrder;

import java.util.List;

public interface FinishOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FinishOrder record);

    int insertSelective(FinishOrder record);

    FinishOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FinishOrder record);

    int updateByPrimaryKey(FinishOrder record);

    List<FinishOrder> selectAll();
}