package com.maple.vo;

/**
 * Created by Maple.Ran on 2017/5/26.
 */
public class DriverListVo {
    private Integer driverId;
    private String driverName;
    private String phoneNum;
    private String coModelType;
    private String driverStatus;
    private String periodStartDate;
    private String carName;
    private String plateNum;
    private String ticketScore;
    private String ticketMoney;


    public String getCoModelType() {
        return coModelType;
    }

    public void setCoModelType(String coModelType) {
        this.coModelType = coModelType;
    }

    public String getTicketScore() {
        return ticketScore;
    }

    public void setTicketScore(String ticketScore) {
        this.ticketScore = ticketScore;
    }

    public String getTicketMoney() {
        return ticketMoney;
    }

    public void setTicketMoney(String ticketMoney) {
        this.ticketMoney = ticketMoney;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(String periodStartDate) {
        this.periodStartDate = periodStartDate;
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

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(String driverStatus) {
        this.driverStatus = driverStatus;
    }
}
