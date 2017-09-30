package com.maple.vo;

import com.maple.pojo.Driver;

import java.util.List;

/**
 * Created by Maple.Ran on 2017/6/1.
 */
public class CarListVo {

    private Integer carId;
    private String branch;//分部
    private String carStatus;//车辆状态
    private String plateNum;
    private String carName;//车型
    private List<DriverCarListVo> driverCarListVoList;//绑定人集合
    private String ticket;
    private String pickDate;//提车日期

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(String carStatus) {
        this.carStatus = carStatus;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public List<DriverCarListVo> getDriverCarListVoList() {
        return driverCarListVoList;
    }

    public void setDriverCarListVoList(List<DriverCarListVo> driverCarListVoList) {
        this.driverCarListVoList = driverCarListVoList;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getPickDate() {
        return pickDate;
    }

    public void setPickDate(String pickDate) {
        this.pickDate = pickDate;
    }
}
