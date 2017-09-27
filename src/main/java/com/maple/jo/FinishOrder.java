package com.maple.jo;

import java.math.BigDecimal;
import java.util.Date;

public class FinishOrder {
    private Integer id;

    private Long driverId;

    private String phone;

    private String name;

    private String joinModelName;

    private String checkLevelName;

    private Date date;

    private BigDecimal finishFlowfee;

    private Integer finishFinishCnt;

    private BigDecimal finishServeTime;

    private BigDecimal finishOnlineTime;

    private BigDecimal finishWorkDistance;

    private BigDecimal finishFeeTime;

    private BigDecimal finishServeDistance;

    public FinishOrder(Integer id, Long driverId, String phone, String name, String joinModelName, String checkLevelName, Date date, BigDecimal finishFlowfee, Integer finishFinishCnt, BigDecimal finishServeTime, BigDecimal finishOnlineTime, BigDecimal finishWorkDistance, BigDecimal finishFeeTime, BigDecimal finishServeDistance) {
        this.id = id;
        this.driverId = driverId;
        this.phone = phone;
        this.name = name;
        this.joinModelName = joinModelName;
        this.checkLevelName = checkLevelName;
        this.date = date;
        this.finishFlowfee = finishFlowfee;
        this.finishFinishCnt = finishFinishCnt;
        this.finishServeTime = finishServeTime;
        this.finishOnlineTime = finishOnlineTime;
        this.finishWorkDistance = finishWorkDistance;
        this.finishFeeTime = finishFeeTime;
        this.finishServeDistance = finishServeDistance;
    }

    public FinishOrder() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getJoinModelName() {
        return joinModelName;
    }

    public void setJoinModelName(String joinModelName) {
        this.joinModelName = joinModelName == null ? null : joinModelName.trim();
    }

    public String getCheckLevelName() {
        return checkLevelName;
    }

    public void setCheckLevelName(String checkLevelName) {
        this.checkLevelName = checkLevelName == null ? null : checkLevelName.trim();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getFinishFlowfee() {
        return finishFlowfee;
    }

    public void setFinishFlowfee(BigDecimal finishFlowfee) {
        this.finishFlowfee = finishFlowfee;
    }

    public Integer getFinishFinishCnt() {
        return finishFinishCnt;
    }

    public void setFinishFinishCnt(Integer finishFinishCnt) {
        this.finishFinishCnt = finishFinishCnt;
    }

    public BigDecimal getFinishServeTime() {
        return finishServeTime;
    }

    public void setFinishServeTime(BigDecimal finishServeTime) {
        this.finishServeTime = finishServeTime;
    }

    public BigDecimal getFinishOnlineTime() {
        return finishOnlineTime;
    }

    public void setFinishOnlineTime(BigDecimal finishOnlineTime) {
        this.finishOnlineTime = finishOnlineTime;
    }

    public BigDecimal getFinishWorkDistance() {
        return finishWorkDistance;
    }

    public void setFinishWorkDistance(BigDecimal finishWorkDistance) {
        this.finishWorkDistance = finishWorkDistance;
    }

    public BigDecimal getFinishFeeTime() {
        return finishFeeTime;
    }

    public void setFinishFeeTime(BigDecimal finishFeeTime) {
        this.finishFeeTime = finishFeeTime;
    }

    public BigDecimal getFinishServeDistance() {
        return finishServeDistance;
    }

    public void setFinishServeDistance(BigDecimal finishServeDistance) {
        this.finishServeDistance = finishServeDistance;
    }
}