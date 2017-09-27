package com.maple.dao;

import com.maple.pojo.Ticket;
import org.eclipse.jetty.util.DateCache;

public interface TicketMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Ticket record);

    int insertSelective(Ticket record);

    Ticket selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Ticket record);

    int updateByPrimaryKey(Ticket record);

    Ticket selectByCarId(Integer carId);
}