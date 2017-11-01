package com.maple.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class PeriodPayment{
    private Integer id;

    private Integer driverId;

    private Integer carId;

    private BigDecimal payment;

    private String payer;

    private Integer paymentPlatform;

    private String accountNumber;

    private String platformNumber;

    private Integer platformStatus;

    private String comment;

    private Date payTime;

    private Date createTime;

    private Date updateTime;

    public PeriodPayment(Integer id, Integer driverId, Integer carId, BigDecimal payment, String payer, Integer paymentPlatform, String accountNumber, String platformNumber, Integer platformStatus, String comment, Date payTime, Date createTime, Date updateTime) {
        this.id = id;
        this.driverId = driverId;
        this.carId = carId;
        this.payment = payment;
        this.payer = payer;
        this.paymentPlatform = paymentPlatform;
        this.accountNumber = accountNumber;
        this.platformNumber = platformNumber;
        this.platformStatus = platformStatus;
        this.comment = comment;
        this.payTime = payTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public PeriodPayment() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public Integer getPaymentPlatform() {
        return paymentPlatform;
    }

    public void setPaymentPlatform(Integer paymentPlatform) {
        this.paymentPlatform = paymentPlatform;
    }

    public String getPlatformNumber() {
        return platformNumber;
    }

    public void setPlatformNumber(String platformNumber) {
        this.platformNumber = platformNumber == null ? null : platformNumber.trim();
    }

    public Integer getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(Integer platformStatus) {
        this.platformStatus = platformStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}