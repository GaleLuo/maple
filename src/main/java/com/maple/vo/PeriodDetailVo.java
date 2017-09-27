package com.maple.vo;

import java.math.BigDecimal;

/**
 * Created by Maple.Ran on 2017/6/5.
 */
public class PeriodDetailVo {

    private BigDecimal periodPayment;//每月金额
    private String periodStartDate;
    private String periodEndDate;


    public BigDecimal getPeriodPayment() {
        return periodPayment;
    }

    public void setPeriodPayment(BigDecimal periodPayment) {
        this.periodPayment = periodPayment;
    }

    public String getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(String periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public String getPeriodEndDate() {
        return periodEndDate;
    }

    public void setPeriodEndDate(String periodEndDate) {
        this.periodEndDate = periodEndDate;
    }
}
