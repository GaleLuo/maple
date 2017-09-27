package com.maple.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class PeriodPlan {
    private Integer id;

    private Integer coModelId;

    private BigDecimal amount;

    private Date startDate;

    private Date endDate;

    private Date createTime;

    private Date updateTime;

    public PeriodPlan(Integer id, Integer coModelId, BigDecimal amount, Date startDate, Date endDate, Date createTime, Date updateTime) {
        this.id = id;
        this.coModelId = coModelId;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public PeriodPlan() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCoModelId() {
        return coModelId;
    }

    public void setCoModelId(Integer coModelId) {
        this.coModelId = coModelId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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