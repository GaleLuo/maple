package com.maple.vo;

import com.maple.util.BigDecimalUtil;

import java.math.BigDecimal;

/**
 * Created by Maple.Ran on 2017/6/22.
 */
public class PeriodPaymentGeneralListVo {
    private String date;//xxxx年第xx周
    private String startDate;//起始日期
    private String endDate;//结束日期
    private BigDecimal amountReceivable;//应收金额
    private BigDecimal amountReceived;//已收金额
    private BigDecimal wechatReceived;//微信已收金额
    private BigDecimal alipayReceived;//支付宝已收金额
    private BigDecimal bankReceived;//银行转账已收金额
    private BigDecimal cashReceived;//现金已收金额
    private BigDecimal difference;//差额
    private Integer driverNoReceivable;//应收司机
    private Integer driverNoReceived;//已收司机


    public BigDecimal getWechatReceived() {
        return wechatReceived;
    }

    public void setWechatReceived(BigDecimal wechatReceived) {
        this.wechatReceived = wechatReceived;
    }

    public BigDecimal getAlipayReceived() {
        return alipayReceived;
    }

    public void setAlipayReceived(BigDecimal alipayReceived) {
        this.alipayReceived = alipayReceived;
    }

    public BigDecimal getBankReceived() {
        return bankReceived;
    }

    public void setBankReceived(BigDecimal bankReceived) {
        this.bankReceived = bankReceived;
    }

    public BigDecimal getCashReceived() {
        return cashReceived;
    }

    public void setCashReceived(BigDecimal cashReceived) {
        this.cashReceived = cashReceived;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public BigDecimal getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(BigDecimal amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public BigDecimal getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(BigDecimal amountReceived) {
        this.amountReceived = amountReceived;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    public Integer getDriverNoReceivable() {
        return driverNoReceivable;
    }

    public void setDriverNoReceivable(Integer driverNoReceivable) {
        this.driverNoReceivable = driverNoReceivable;
    }

    public Integer getDriverNoReceived() {
        return driverNoReceived;
    }

    public void setDriverNoReceived(Integer driverNoReceived) {
        this.driverNoReceived = driverNoReceived;
    }
}
