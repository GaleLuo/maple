package com.maple.vo;

import com.maple.pojo.Driver;

import java.util.List;

/**
 * Created by Maple.Ran on 2017/6/1.
 */
public class CarListVo {

    private Integer id;
    private String plateNumber;
    private String name;//车型
    private List<Driver> driverList;//绑定人集合
    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Driver> getDriverList() {
        return driverList;
    }

    public void setDriverList(List<Driver> driverList) {
        this.driverList = driverList;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
