package com.maple.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class Insurance {
    private Integer id;

    private Integer carId;

    private Integer insuranceType;

    private String companyName;

    private BigDecimal insurancePrice;

    private Date expireDate;

    private Date createTime;

    private Date updateTime;

    public Insurance(Integer id, Integer carId, Integer insuranceType, String companyName, BigDecimal insurancePrice, Date expireDate, Date createTime, Date updateTime) {
        this.id = id;
        this.carId = carId;
        this.insuranceType = insuranceType;
        this.companyName = companyName;
        this.insurancePrice = insurancePrice;
        this.expireDate = expireDate;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Insurance() {
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

    public Integer getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(Integer insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public BigDecimal getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(BigDecimal insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
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