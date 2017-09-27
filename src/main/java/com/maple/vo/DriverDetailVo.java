package com.maple.vo;

import java.math.BigDecimal;

/**
 * Created by Maple.Ran on 2017/5/27.
 */
public class DriverDetailVo {

    private Integer id;
    private String name;
    private String idNumber;
    private String personalPhone;
    private Integer operationStatus;
    private String createTime;

    private String carName; //车型

    private Integer modelTypeCode;//合作模式码
    private String modelTypeDesc;//合作模式描述

    private BigDecimal total_amount;//总计金额
    private BigDecimal down_amount;//首付金额

    public String getPersonalPhone() {
        return personalPhone;
    }

    public void setPersonalPhone(String personalPhone) {
        this.personalPhone = personalPhone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Integer getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(Integer operationStatus) {
        this.operationStatus = operationStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public Integer getModelTypeCode() {
        return modelTypeCode;
    }

    public void setModelTypeCode(Integer modelTypeCode) {
        this.modelTypeCode = modelTypeCode;
    }

    public String getModelTypeDesc() {
        return modelTypeDesc;
    }

    public void setModelTypeDesc(String modelTypeDesc) {
        this.modelTypeDesc = modelTypeDesc;
    }

    public BigDecimal getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(BigDecimal total_amount) {
        this.total_amount = total_amount;
    }

    public BigDecimal getDown_amount() {
        return down_amount;
    }

    public void setDown_amount(BigDecimal down_amount) {
        this.down_amount = down_amount;
    }
}
