package com.maple.service;

import com.maple.common.ServerResponse;

/**
 * Created by Maple.Ran on 2017/5/24.
 */
public interface ICartService {
    ServerResponse list(Integer userId);

    ServerResponse add(Integer userId, Integer productId, Integer count);

    ServerResponse update(Integer userId, Integer productId, Integer count);

    ServerResponse delete(Integer userId, String productIds);

    ServerResponse selectUnselect(Integer userId, Integer productId,Integer checkStatus);

    ServerResponse getProductCount(Integer userId);
}
