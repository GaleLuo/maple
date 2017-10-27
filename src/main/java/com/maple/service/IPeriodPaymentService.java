package com.maple.service;

import com.maple.common.ServerResponse;
import com.maple.pojo.User;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Maple.Ran on 2017/6/13.
 */
public interface IPeriodPaymentService {


    ServerResponse list(Integer branch, String date,String driverName,Integer coModelType, Integer payStatus, int pageNum, int pageSize);

    ServerResponse generalList(Integer branch,Long startDate,Long endDate,Integer coModelType, int pageNum, int pageSize);

    ServerResponse currentPaymentDetail(Integer driverId, String date);

    ServerResponse driverTotalPayment(Integer driverId);

    ServerResponse addOrUpdate(User user,Boolean addToAccount,Integer paymentId, Integer driverId, BigDecimal payment, String payer,Integer paymentPlatform, String platformNum,Long payTime,String comment);

    ServerResponse paymentList(Integer branch,Long startTime, Long endTime, String driverName, Integer coModelType, String payer,Integer platformStatus, Integer paymentPlatform,int pageNum, int pageSize);

    ServerResponse updatePaymentStatus(Integer[] paymentIdArray, Integer paymentStatus);
}
