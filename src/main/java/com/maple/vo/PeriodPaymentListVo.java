package com.maple.vo;

import java.math.BigDecimal;

/**
 * Created by Maple.Ran on 2017/6/20.
 */
public class PeriodPaymentListVo {
    private int driverId;
    private int carId;
    private String driverName;
    private String phoneNum;
    private String plateNum;
    private String carName;
    //todo 删除这个vo 改为dueAmount
    private PlanDetailVo planDetailVo;//当期应还款金额
    private BigDecimal payment;//当期已还款金额


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

    public PlanDetailVo getPlanDetailVo() {
        return planDetailVo;
    }

    public void setPlanDetailVo(PlanDetailVo planDetailVo) {
        this.planDetailVo = planDetailVo;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

}
