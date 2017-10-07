package com.maple.vo;

import com.maple.pojo.Driver;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Maple.Ran on 2017/10/3.
 */
//未使用
public class CarVo {
    private List<Driver> driverList;
    private String vin;
    private String plateNum;
    private String carStatus;
    private String carName;
    private String startDate;
    private String endDate;
    private String coModelType;
    private String periodPercentage;//资金完成度
    private BigDecimal totalAmount;
    private BigDecimal downAmount;
    private Integer periodNum;//总周数
    private BigDecimal overdueAmount;//现在逾期金额
    private BigDecimal balance;//余额
    private String ticket;//违章情况
    private String ticketUpdateTime;


    public List<Driver> getDriverList() {
        return driverList;
    }

    public void setDriverList(List<Driver> driverList) {
        this.driverList = driverList;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public String getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(String carStatus) {
        this.carStatus = carStatus;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCoModelType() {
        return coModelType;
    }

    public void setCoModelType(String coModelType) {
        this.coModelType = coModelType;
    }

    public String getPeriodPercentage() {
        return periodPercentage;
    }

    public void setPeriodPercentage(String periodPercentage) {
        this.periodPercentage = periodPercentage;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDownAmount() {
        return downAmount;
    }

    public void setDownAmount(BigDecimal downAmount) {
        this.downAmount = downAmount;
    }

    public Integer getPeriodNum() {
        return periodNum;
    }

    public void setPeriodNum(Integer periodNum) {
        this.periodNum = periodNum;
    }

    public BigDecimal getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(BigDecimal overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getTicketUpdateTime() {
        return ticketUpdateTime;
    }

    public void setTicketUpdateTime(String ticketUpdateTime) {
        this.ticketUpdateTime = ticketUpdateTime;
    }
}
