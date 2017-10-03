package com.maple.vo;

import java.math.BigDecimal;

/**
 * Created by Maple.Ran on 2017/6/22.
 */
public class PaymentDetailVo {
    private int id;
    private BigDecimal payment;//付款额
    private String payer;//付款人
    private String paymentPlatform;//付款平台
    private int paymentPlatformCode;//平台流水号
    private String paymentStatus;//付款状态
    private String payTime;
    private String updateTime;
    private String comment;

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPaymentPlatformCode() {
        return paymentPlatformCode;
    }

    public void setPaymentPlatformCode(int paymentPlatformCode) {
        this.paymentPlatformCode = paymentPlatformCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public String getPaymentPlatform() {
        return paymentPlatform;
    }

    public void setPaymentPlatform(String paymentPlatform) {
        this.paymentPlatform = paymentPlatform;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }
}

