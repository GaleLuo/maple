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

    List<Car> selectByMultiParam(@Param("carIdList") List<Integer> carIdList, @Param("driverName") String driverName, @Param("plateNumber") String plateNumber, @Param("name") String name, @Param("createDateT") Date createDateT, @Param("createDateB") Date createDateB);

    Integer selectByVin(String stringCellValue);
}