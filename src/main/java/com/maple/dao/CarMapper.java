package com.maple.dao;

import com.maple.pojo.Car;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CarMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Car record);

    int insertSelective(Car record);

    Car selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Car record);

    int updateByPrimaryKey(Car record);

    Car selectByVin(String stringCellValue);

    List<Car> selectByMultiParam(@Param("userId") Integer userId, @Param("driverName") String driverName, @Param("branch") Integer branch, @Param("carStatus") Integer carStatus, @Param("plateNumber") String plateNumber, @Param("carName") String carName, @Param("orderBy") String orderBy);

    List<Car> selectCarListForTicket();

    List<Car> selectWhereUnchecked();

    Car selectbyPlateNumber(String plateNumber);

    Integer countTotalByYearMonth(@Param("date") Date date, @Param("branch") Integer branch);

    Integer countAddByYearMonth(@Param("thisMonth") Date thisMonth, @Param("nextMonth") Date nextMonth, @Param("branch") Integer branch);
}