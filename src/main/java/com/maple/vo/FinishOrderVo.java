package com.maple.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Maple.Ran on 2017/7/24.
 */
public class FinishOrderVo {
    private Integer id;

    private Long driverId;

    private String phone;

    private String name;

    private String joinModelName;

    private String checkLevelName;

    private String date;

    private BigDecimal finishFlowfee;

    private Integer finishFinishCnt;

    private BigDecimal finishServeTime;

    private BigDecimal finishOnlineTime;

    private BigDecimal finishWorkDistance;

    private BigDecimal finishFeeTime;

    private BigDecimal finishServeDistance;

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
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJoinModelName() {
        return joinModelName;
    }

    public void setJoinModelName(String joinModelName) {
        this.joinModelName = joinModelName;
    }

    public String getCheckLevelName() {
        return checkLevelName;
    }

    public void setCheckLevelName(String checkLevelName) {
        this.checkLevelName = checkLevelName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
