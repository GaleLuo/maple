package com.maple.service;

import com.maple.common.ServerResponse;
import com.maple.pojo.CoModel;
import com.maple.vo.CoModelDetailVo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Maple.Ran on 2017/6/4.
 */
public interface ICoModelService {
    ServerResponse save(Integer userId, CoModel coModel);

    ServerResponse<CoModelDetailVo> detail(Integer userId, Integer carId);

    ServerResponse update(CoModel coModel);

    ServerResponse summary(Integer coModelId);

    ServerResponse addOrUpdate(Integer driverId,Integer coModelId,Integer carId,Long periodStartDate,Long periodEndDate, Integer modelType, BigDecimal totalAmount, BigDecimal downAmount, BigDecimal finalAmount, Date periodPlanStartDate, Integer periodNum, String comment);
}
