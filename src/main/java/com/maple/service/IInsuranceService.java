package com.maple.service;

import com.maple.common.ServerResponse;
import com.maple.pojo.Insurance;

/**
 * Created by Maple.Ran on 2017/5/31.
 */
public interface IInsuranceService {
    ServerResponse list(Integer userId, Integer carId, Integer insuranceType, String companyName, String createDateT, String createDateB, int pageNum, int pageSize);

    ServerResponse saveOrUpdateInsurance(Insurance insurance);
}
