package com.maple.vo;

import java.math.BigDecimal;

/**
 * Created by Maple.Ran on 2017/7/17.
 */
public class DriverVo {

    private String driverName;
    private String phoneNum;
    private String idNum;
    private String vin;
    private String plateNum;
    private String driverStatus;
    private String carName;
    private String startDate;
    private String endDate;
    private String coModelType;
    private String periodPercentage;//资金完成度
    private BigDecimal totalAmount;
    private BigDecimal downAmount;
    private Integer periodNum;//总周数
    private Integer overdueNum;//逾期次数
    private BigDecimal overdueAmount;//现在逾期金额
    private BigDecimal balance;//余额
    private String ticketScore;//违章分数
    private String ticketMoney;//违章罚金
    private String ticketUpdateTime;

    public String getTicketUpdateTime() {
        return ticketUpdateTime;
    }

    public void setTicketUpdateTime(String ticketUpdateTime) {
        this.ticketUpdateTime = ticketUpdateTime;
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

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public String getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(String driverStatus) {
        this.driverStatus = driverStatus;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
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

    public Integer getOverdueNum() {
        return overdueNum;
    }

    public void setOverdueNum(Integer overdueNum) {
        this.overdueNum = overdueNum;
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
}
