package com.maple.pojo;

import java.util.Date;

public class Car {
    private Integer id;

    private Integer carStatus;

    private String name;

    private String plateNumber;

    private String engineNumber;

    private String vin;

    private Date pickDate;

    private String carLicenceFrontImage;

    private String carLicenceBackImage;

    private String cInsuranceImage;

    private String mInsuranceImage;

    private String gpsNumber;

    private String gpsPhone;

    private Date createTime;

    private Date updateTime;

    public Car(Integer id, Integer carStatus, String name, String plateNumber, String engineNumber, String vin, Date pickDate, String carLicenceFrontImage, String carLicenceBackImage, String cInsuranceImage, String mInsuranceImage, String gpsNumber, String gpsPhone, Date createTime, Date updateTime) {
        this.id = id;
        this.carStatus = carStatus;
        this.name = name;
        this.plateNumber = plateNumber;
        this.engineNumber = engineNumber;
        this.vin = vin;
        this.pickDate = pickDate;
        this.carLicenceFrontImage = carLicenceFrontImage;
        this.carLicenceBackImage = carLicenceBackImage;
        this.cInsuranceImage = cInsuranceImage;
        this.mInsuranceImage = mInsuranceImage;
        this.gpsNumber = gpsNumber;
        this.gpsPhone = gpsPhone;
        this.createTime = createTime;
        this.updateTime = updateTime;
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

    public String getcInsuranceImage() {
        return cInsuranceImage;
    }

    public void setcInsuranceImage(String cInsuranceImage) {
        this.cInsuranceImage = cInsuranceImage == null ? null : cInsuranceImage.trim();
    }

    public String getmInsuranceImage() {
        return mInsuranceImage;
    }

    public void setmInsuranceImage(String mInsuranceImage) {
        this.mInsuranceImage = mInsuranceImage == null ? null : mInsuranceImage.trim();
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
}