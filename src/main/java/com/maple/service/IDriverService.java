package com.maple.service;

import com.maple.common.ServerResponse;
import com.maple.pojo.Driver;

/**
 * Created by Maple.Ran on 2017/5/26.
 */
public interface IDriverService {
    ServerResponse addOrUpdate(Integer userId, Driver driver);

    ServerResponse setDriverStatus(Integer userId, String driverIds,Integer status);

    ServerResponse delete(Integer userId, String driverIds, Integer pageNum, Integer pageSize);

    ServerResponse list(Integer userId,String plateNum, String driverName, String phoneNum, Integer driverStatus, Integer coModelType, String orderBy, int pageNum, int pageSize);

    ServerResponse search(Integer userId, String createDateTop,String createDateBut, String name, String status, int pageNum, int pageSize);

    ServerResponse detail(Integer userId, Integer driverId);

    ServerResponse manageDriverStatus(String driverIds, Integer status);

    ServerResponse addAccount(Integer driverId,String name, Integer platformCode, String account);

    ServerResponse getAccountList(Integer driverId);

    ServerResponse summary(Integer userId, Integer driverId);
}
