package com.maple.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Maple.Ran on 2017/9/21.
 */
public class DriverTotalPaymentListVo {
    private String dueDate ;//应付周

    private BigDecimal dueAmount;//应付金额

    private BigDecimal receivedAmount;//已收金额

    private String status;

    private List<PaymentDetailVo> paymentDetailVoList;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
    }

    public BigDecimal getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(BigDecimal receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public List<PaymentDetailVo> getPaymentDetailVoList() {
        return paymentDetailVoList;
    }

    public void setPaymentDetailVoList(List<PaymentDetailVo> paymentDetailVoList) {
        this.paymentDetailVoList = paymentDetailVoList;
    }
}
