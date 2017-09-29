package com.maple.service;

import com.maple.common.ServerResponse;
import com.maple.pojo.Car;

/**
 * Created by Maple.Ran on 2017/6/1.
 */
public interface ICarService {
    ServerResponse save(Integer productId);

    ServerResponse update(Integer userId, Car car);

    ServerResponse list(Integer userId,Integer branch,Integer carStatus, String plateNumber,String carName,String orderBy, int pageNum, int pageSize);
}
