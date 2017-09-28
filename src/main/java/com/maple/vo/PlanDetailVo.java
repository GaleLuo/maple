package com.maple.vo;

import java.math.BigDecimal;

/**
 * Created by Maple.Ran on 2017/6/5.
 */
public class PlanDetailVo {

    private BigDecimal planAmount;//每期金额
    private String planStartDate;
    private String planEndDate;


    public BigDecimal getPlanAmount() {
        return planAmount;
    }

    public void setPlanAmount(BigDecimal planAmount) {
        this.planAmount = planAmount;
    }

    public String getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(String planStartDate) {
        this.planStartDate = planStartDate;
    }

    public String getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(String planEndDate) {
        this.planEndDate = planEndDate;
    }
}
