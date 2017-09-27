package com.maple.service;

import com.maple.common.ServerResponse;
import com.maple.pojo.CoModel;
import com.maple.vo.CoModelDetailVo;

/**
 * Created by Maple.Ran on 2017/6/4.
 */
public interface ICoModelService {
    ServerResponse save(Integer userId, CoModel coModel);

    ServerResponse<CoModelDetailVo> detail(Integer userId, Integer carId);

    ServerResponse update(CoModel coModel);
}
