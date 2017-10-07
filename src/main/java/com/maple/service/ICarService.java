package com.maple.service;

import com.maple.common.ServerResponse;

import java.util.Date;

/**
 * Created by Maple.Ran on 2017/6/1.
 */
public interface ICarService {
    ServerResponse save(Integer productId);

    ServerResponse addOrUpdate(Integer userId, Integer id, Integer branch, Integer carStatus, String name, String plateNumber, String engineNumber, String vin, Date pickDate, Date transferDate, Date redeemDate, String gpsNumber, String gpsPhone);

    ServerResponse list(Integer userId,String driverName,Integer branch,Integer carStatus, String plateNumber,String carName,String orderBy, int pageNum, int pageSize);

    ServerResponse summary(Integer carId);

    ServerResponse bind(Integer driverId, Integer carId);
}
