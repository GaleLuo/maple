package com.maple.dao;

import com.maple.pojo.CoModel;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface CoModelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CoModel record);

    int insertSelective(CoModel record);

    CoModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CoModel record);

    int updateByPrimaryKey(CoModel record);

    CoModel selectByCarId(Integer carId);

    BigDecimal findAmountReceivable(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("coModelType") Integer coModelType);

    CoModel selectByCarIdByNow(Integer carId);
}