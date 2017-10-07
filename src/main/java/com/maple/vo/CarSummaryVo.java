package com.maple.vo;

/**
 * Created by Maple.Ran on 2017/10/6.
 */
public class CarSummaryVo {
    private Integer carId;
    private String branch;
    private String carStatus;
    private String carName;
    private String plateNum;
    private String engineNum;
    private String vin;
    private String pickDate;
    private String transferDate;//过户日期
    private String redeemDate;//赎回日期
    private String gpsNum;
    private String gpsPhoneNum;

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(String carStatus) {
        this.carStatus = carStatus;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public String getEngineNum() {
        return engineNum;
    }

    public void setEngineNum(String engineNum) {
        this.engineNum = engineNum;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getPickDate() {
        return pickDate;
    }

    public void setPickDate(String pickDate) {
        this.pickDate = pickDate;
    }

    public String getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    public String getRedeemDate() {
        return redeemDate;
    }

    public void setRedeemDate(String redeemDate) {
        this.redeemDate = redeemDate;
    }

    public String getGpsNum() {
        return gpsNum;
    }

    public void setGpsNum(String gpsNum) {
        this.gpsNum = gpsNum;
    }

    public String getGpsPhoneNum() {
        return gpsPhoneNum;
    }

    public void setGpsPhoneNum(String gpsPhoneNum) {
        this.gpsPhoneNum = gpsPhoneNum;
    }
}
