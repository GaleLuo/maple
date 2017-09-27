package com.maple.service;

import com.maple.common.ServerResponse;
import com.maple.pojo.ContactInfo;

/**
 * Created by Maple.Ran on 2017/6/1.
 */
public interface IContactInfoService {
    ServerResponse saveOrUpdate(ContactInfo contactInfo);

    ServerResponse detail(Integer userId, Integer driverId);
}
