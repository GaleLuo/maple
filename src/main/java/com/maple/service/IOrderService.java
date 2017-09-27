package com.maple.service;

import com.maple.common.ServerResponse;

/**
 * Created by Maple.Ran on 2017/5/24.
 */
public interface IOrderService {
    ServerResponse createOrder(Integer userId, Integer driverId);

    ServerResponse getOrderList(Integer userId, int pageNum, int pageSize);
}
