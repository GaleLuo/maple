package com.maple.vo;

import java.util.List;

/**
 * Created by Maple.Ran on 2017/9/21.
 */
public class DriverTotalPaymentListVo {
    private String dueDate ;//应付周

    private List<PaymentDetailVo> paymentDetailVoList;

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
