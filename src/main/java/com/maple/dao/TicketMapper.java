package com.maple.dao;

import com.maple.pojo.Ticket;
import org.eclipse.jetty.util.DateCache;

import java.util.Date;
import java.util.List;

public interface TicketMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Ticket record);

    int insertSelective(Ticket record);

    Ticket selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Ticket record);

    int updateByPrimaryKey(Ticket record);

    Ticket selectByCarId(Integer carId);

    List<Integer> selectCarIdListForSms();

    void deleteBeforeHalfMonth();

    int selectTotalIndexByDate(Date date);
}