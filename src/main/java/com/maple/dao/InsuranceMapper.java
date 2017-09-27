package com.maple.dao;

import com.maple.pojo.Insurance;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface InsuranceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Insurance record);

    int insertSelective(Insurance record);

    Insurance selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Insurance record);

    int updateByPrimaryKey(Insurance record);

    List<Insurance> selectByMultiParams(@Param("carIdList") List<Integer> carIdList, @Param("carId") Integer carId, @Param("insuranceType") Integer insuranceType, @Param("companyName") String companyName, @Param("createDateTop") Date createDateTop, @Param("createDateBut") Date createDateBut);
}