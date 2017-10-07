package com.maple.pojo;

import java.util.Date;

public class Car {
    private Integer id;

    private Integer branch;

    private Integer carStatus;

    private String name;

    private String plateNumber;

    private String engineNumber;

    private String vin;

    private Date pickDate;

    private String carLicenceFrontImage;

    private String carLicenceBackImage;

    private Date transferDate;

    private Date redeemDate;

    private String gpsNumber;

    private String gpsPhone;

    private Date createTime;

    private Date updateTime;

    public Car(Integer id, Integer branch, Integer carStatus, String name, String plateNumber, String engineNumber, String vin, Date pickDate, String carLicenceFrontImage, String carLicenceBackImage, Date transferDate, Date redeemDate, String gpsNumber, String gpsPhone, Date createTime, Date updateTime) {
        this.id = id;
        this.branch = branch;
        this.carStatus = carStatus;
        this.name = name;
        this.plateNumber = plateNumber;
        this.engineNumber = engineNumber;
        this.vin = vin;
        this.pickDate = pickDate;
        this.carLicenceFrontImage = carLicenceFrontImage;
        this.carLicenceBackImage = carLicenceBackImage;
        this.transferDate = transferDate;
        this.redeemDate = redeemDate;
        this.gpsNumber = gpsNumber;
        this.gpsPhone = gpsPhone;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Integer getBranch() {
        return branch;
    }

    public void setBranch(Integer branch) {
        this.branch = branch;
    }

    public Integer getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(Integer carStatus) {
        this.carStatus = carStatus;
    }

    public Car() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber == null ? null : plateNumber.trim();
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber == null ? null : engineNumber.trim();
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin == null ? null : vin.trim();
    }

    public Date getPickDate() {
        return pickDate;
    }

    public void setPickDate(Date pickDate) {
        this.pickDate = pickDate;
    }

    public String getCarLicenceFrontImage() {
        return carLicenceFrontImage;
    }

    public void setCarLicenceFrontImage(String carLicenceFrontImage) {
        this.carLicenceFrontImage = carLicenceFrontImage == null ? null : carLicenceFrontImage.trim();
    }

    public String getCarLicenceBackImage() {
        return carLicenceBackImage;
    }

    public void setCarLicenceBackImage(String carLicenceBackImage) {
        this.carLicenceBackImage = carLicenceBackImage == null ? null : carLicenceBackImage.trim();
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public Date getRedeemDate() {
        return redeemDate;
    }

    public void setRedeemDate(Date redeemDate) {
        this.redeemDate = redeemDate;
    }

    public String getGpsNumber() {
        return gpsNumber;
    }

    public void setGpsNumber(String gpsNumber) {
        this.gpsNumber = gpsNumber == null ? null : gpsNumber.trim();
    }

    public String getGpsPhone() {
        return gpsPhone;
    }

    public void setGpsPhone(String gpsPhone) {
        this.gpsPhone = gpsPhone == null ? null : gpsPhone.trim();
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

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", branch=" + branch +
                ", carStatus=" + carStatus +
                ", name='" + name + '\'' +
                ", plateNumber='" + plateNumber + '\'' +
                ", engineNumber='" + engineNumber + '\'' +
                ", vin='" + vin + '\'' +
                ", pickDate=" + pickDate +
                ", carLicenceFrontImage='" + carLicenceFrontImage + '\'' +
                ", carLicenceBackImage='" + carLicenceBackImage + '\'' +
                ", transferDate=" + transferDate +
                ", redeemDate=" + redeemDate +
                ", gpsNumber='" + gpsNumber + '\'' +
                ", gpsPhone='" + gpsPhone + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}