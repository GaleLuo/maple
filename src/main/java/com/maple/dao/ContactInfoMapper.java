package com.maple.dao;

import com.maple.pojo.ContactInfo;

public interface ContactInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ContactInfo record);

    int insertSelective(ContactInfo record);

    ContactInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ContactInfo record);

    int updateByPrimaryKey(ContactInfo record);

    ContactInfo selectByDriverId(Integer driverId);
}