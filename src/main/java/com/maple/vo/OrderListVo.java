package com.maple.vo;

import java.math.BigDecimal;

/**
 * Created by Maple.Ran on 2017/8/18.
 */
public class OrderListVo {
    private Integer orderNum;
    private String driverName;
    private String phoneNum;
    private String orderStatus;
    private String productName;
    private String coModelType;
    private BigDecimal downAmount;
    private BigDecimal totalAmount;
    private String saleManName;
    private PayInfoVo payInfoVo;


    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCoModelType() {
        return coModelType;
    }

    public void setCoModelType(String coModelType) {
        this.coModelType = coModelType;
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

    public String getSaleManName() {
        return saleManName;
    }

    public void setSaleManName(String saleManName) {
        this.saleManName = saleManName;
    }

    public PayInfoVo getPayInfoVo() {
        return payInfoVo;
    }

    public void setPayInfoVo(PayInfoVo payInfoVo) {
        this.payInfoVo = payInfoVo;
    }
}
