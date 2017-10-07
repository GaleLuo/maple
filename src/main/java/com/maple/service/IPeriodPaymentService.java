package com.maple.service;

import com.maple.common.ServerResponse;
import com.maple.pojo.User;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Maple.Ran on 2017/6/13.
 */
public interface IPeriodPaymentService {


    ServerResponse list(String date,String driverName,Integer coModelType, Integer payStatus, int pageNum, int pageSize);

    ServerResponse generalList(Long startDate,Long endDate,Integer coModelType, int pageNum, int pageSize);

    ServerResponse currentPaymentDetail(Integer driverId, String date);

    ServerResponse driverTotalPayment(Integer driverId);

    ServerResponse addOrUpdate(User user, Integer driverId, BigDecimal payment, Integer paymentPlatform, String platformNum,String payTime);
}
