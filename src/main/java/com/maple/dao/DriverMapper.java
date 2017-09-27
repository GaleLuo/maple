package com.maple.dao;

import com.maple.pojo.Driver;
import org.apache.ibatis.annotations.Param;
import org.apache.xpath.operations.Bool;

import java.util.Date;
import java.util.List;

public interface DriverMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Driver record);

    int insertSelective(Driver record);

    Driver selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Driver record);

    int updateByPrimaryKey(Driver record);

    Driver selectByDriverIdAndUserId(@Param("userId") Integer userId, @Param("driverId") Integer driverId);

    int setDriverStatusByUserIdAndDriverIds(@Param("userId")Integer userId, @Param("driverIdList") List<String> driverIdList, @Param("status") Integer status);

    int deleteDeletableDriverByUserIdAndDriverIds(@Param("userId")Integer userId, @Param("driverIdList")List<String> driverIdList);

    List<Driver> selectDriverByUserId(Integer userId);

    List<Driver> selectByUserIdCreateDateNameStatusList(@Param("userId")Integer userId,
                                                        @Param("createDateT")Date createDateT,
                                                        @Param("createDateB")Date createDateB,
                                                        @Param("name")String name,
                                                        @Param("statusList")List<Integer> statusList);

    Integer selectUserIdByCarId(Integer carId);

    List<Driver> selectDriverListByCarId(Integer carId);

    List<Integer> selectCarIdListByUserId(Integer userId);

    Integer selectCarIdByDriverId(Integer driverId);

    List<Driver> selectDriverByCoModelType(Integer coModelType);

    List<Driver> selectByCarIdList(List<Integer> carIdList);

    Integer checkId(String idNumber);

    List<Driver> selectDriverReceivable(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("coModelType") Integer coModelType, @Param("driverName") String driverName);

    List<Driver> selectDriverReceived(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("coModelType") Integer coModelType, @Param("driverName") String driverName);

    List<Driver> selectDriverNotReceived(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("coModelType") Integer coModelType, @Param("driverName") String driverName);

    List<Driver> selectDriverReceivedPartly(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("coModelType") Integer coModelType, @Param("driverName") String driverName);

    List<Driver> selectByParams(@Param("driverName") String driverName, @Param("phoneNum") String phoneNum, @Param("driverStatus") Integer driverStatus, @Param("coModelType") Integer coModelType);
}