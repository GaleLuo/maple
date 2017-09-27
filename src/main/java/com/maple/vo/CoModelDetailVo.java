package com.maple.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Maple.Ran on 2017/6/5.
 */
public class CoModelDetailVo {

    private Integer id;
    private Integer carId;
    private Integer modelTypeCode;//合作模式代码
    private String modelTypeDesc;//合作模式描述
    private BigDecimal downAmount;//首付金额
    private BigDecimal totalAmount;//总计金额
    private List<PeriodDetailVo> periodDetailVoList;//分期详情集合
    private BigDecimal finalAmount;//尾款金额
    private BigDecimal managementFee;//管理费金额
    private String deadline;//管理费或尾款交费时间
    private String comment;//备注

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

    public List<PeriodDetailVo> getPeriodDetailVoList() {
        return periodDetailVoList;
    }

    public void setPeriodDetailVoList(List<PeriodDetailVo> periodDetailVoList) {
        this.periodDetailVoList = periodDetailVoList;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public BigDecimal getManagementFee() {
        return managementFee;
    }

    public void setManagementFee(BigDecimal managementFee) {
        this.managementFee = managementFee;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
