package com.maple.vo;

import java.math.BigDecimal;

/**
 * Created by Maple.Ran on 2017/10/5.
 */
public class coModelSummaryVo {
    private Integer coModelId;
    private String modelType;
    private String[] periodDateArray;
    private BigDecimal totalAmount;
    private BigDecimal downAmount;
    private BigDecimal finalAmount;
    private Integer periodNum;
    private String periodPlanStartDate;
    private String comment;

    public Integer getCoModelId() {
        return coModelId;
    }

    public void setCoModelId(Integer coModelId) {
        this.coModelId = coModelId;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String[] getPeriodDateArray() {
        return periodDateArray;
    }

    public void setPeriodDateArray(String[] periodDateArray) {
        this.periodDateArray = periodDateArray;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDownAmount() {
        return downAmount;
    }

    public void setDownAmount(BigDecimal downAmount) {
        this.downAmount = downAmount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public Integer getPeriodNum() {
        return periodNum;
    }

    public void setPeriodNum(Integer periodNum) {
        this.periodNum = periodNum;
    }

    public String getPeriodPlanStartDate() {
        return periodPlanStartDate;
    }

    public void setPeriodPlanStartDate(String periodPlanStartDate) {
        this.periodPlanStartDate = periodPlanStartDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
