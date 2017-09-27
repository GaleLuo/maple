package com.maple.pojo;

import com.maple.util.BigDecimalUtil;

import java.math.BigDecimal;
import java.util.Date;

public class CoModel {
    private Integer id;

    private Integer carId;

    private Integer modelType;

    private BigDecimal downAmount;

    private BigDecimal totalAmount;

    private Integer periodNum;

    private Date periodStartDate;

    private Date periodEndDate;

    private BigDecimal finalAmount;

    private BigDecimal managementFee;

    private Date deadline;

    private String comment;

    private Date createTime;

    private Date updateTime;

    public CoModel(Integer id, Integer carId, Integer modelType, BigDecimal downAmount, BigDecimal totalAmount, Integer periodNum, Date periodStartDate, Date periodEndDate, BigDecimal finalAmount, BigDecimal managementFee, Date deadline, String comment, Date createTime, Date updateTime) {
        this.id = id;
        this.carId = carId;
        this.modelType = modelType;
        this.downAmount = downAmount;
        this.totalAmount = totalAmount;
        this.periodNum = periodNum;
        this.periodStartDate = periodStartDate;
        this.periodEndDate = periodEndDate;
        this.finalAmount = finalAmount;
        this.managementFee = managementFee;
        this.deadline = deadline;
        this.comment = comment;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public CoModel() {
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

    public Integer getModelType() {
        return modelType;
    }

    public void setModelType(Integer modelType) {
        this.modelType = modelType;
    }

    public BigDecimal getDownAmount() {
        return downAmount;
    }

    public void setDownAmount(BigDecimal downAmount) {
        this.downAmount = downAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getPeriodNum() {
        return periodNum;
    }

    public void setPeriodNum(Integer periodNum) {
        this.periodNum = periodNum;
    }

    public Date getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(Date periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public Date getPeriodEndDate() {
        return periodEndDate;
    }

    public void setPeriodEndDate(Date periodEndDate) {
        this.periodEndDate = periodEndDate;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public BigDecimal getManagementFee() {
        return managementFee;
    }

    public void setManagementFee(BigDecimal managementFee) {
        this.managementFee = managementFee;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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