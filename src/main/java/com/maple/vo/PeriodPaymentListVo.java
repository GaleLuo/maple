package com.maple.vo;

import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * Created by Maple.Ran on 2017/6/20.
 */
public class PeriodPaymentListVo implements Comparable<PeriodPaymentListVo>{
    private int driverId;
    private int carId;
    private String driverName;
    private String phoneNum;
    private String plateNum;
    private String carName;
//  private PlanDetailVo planDetailVo;
    //当期应还款金额
    private BigDecimal dueAmount;
    private String dueDate;

    private BigDecimal confirmedPayment;//当期已确认还款金额
    private BigDecimal unconfirmedPayment;//当期为确认还款金额


    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public BigDecimal getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
    }

    public BigDecimal getConfirmedPayment() {
        return confirmedPayment;
    }

    public void setConfirmedPayment(BigDecimal confirmedPayment) {
        this.confirmedPayment = confirmedPayment;
    }

    public BigDecimal getUnconfirmedPayment() {
        return unconfirmedPayment;
    }

    public void setUnconfirmedPayment(BigDecimal unconfirmedPayment) {
        this.unconfirmedPayment = unconfirmedPayment;
    }

    @Override
    public int compareTo(PeriodPaymentListVo o) {
        if (this.dueDate == null || o.dueDate == null) {
            return 1;
        }
        int i = Integer.parseInt(this.dueDate) - Integer.parseInt(o.getDueDate());
        return i;
    }
}
