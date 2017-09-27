package com.maple.pojo;

import java.util.Date;

public class Driver {
    private Integer id;

    private Integer carId;

    private Integer userId;

    private Integer coModelId;

    private String name;

    private String idNumber;

    private String driverCarImage;

    private String driverLicenceFrontImage;

    private String driverLicenceBackImage;

    private String personalPhone;

    private String workPhone;

    private String driverLicenceFileNumber;

    private Integer operationStatus;

    private Integer periodsStatus;

    private Date createTime;

    private Date updateTime;

    public Driver(Integer id, Integer carId, Integer userId, Integer coModelId, String name, String idNumber, String driverCarImage, String driverLicenceFrontImage, String driverLicenceBackImage, String personalPhone, String workPhone, String driverLicenceFileNumber, Integer operationStatus, Integer periodsStatus, Date createTime, Date updateTime) {
        this.id = id;
        this.carId = carId;
        this.userId = userId;
        this.coModelId = coModelId;
        this.name = name;
        this.idNumber = idNumber;
        this.driverCarImage = driverCarImage;
        this.driverLicenceFrontImage = driverLicenceFrontImage;
        this.driverLicenceBackImage = driverLicenceBackImage;
        this.personalPhone = personalPhone;
        this.workPhone = workPhone;
        this.driverLicenceFileNumber = driverLicenceFileNumber;
        this.operationStatus = operationStatus;
        this.periodsStatus = periodsStatus;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Driver() {
        super();
    }

    public Integer getCoModelId() {
        return coModelId;
    }

    public void setCoModelId(Integer coModelId) {
        this.coModelId = coModelId;
    }

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber == null ? null : idNumber.trim();
    }

    public String getDriverCarImage() {
        return driverCarImage;
    }

    public void setDriverCarImage(String driverCarImage) {
        this.driverCarImage = driverCarImage == null ? null : driverCarImage.trim();
    }

    public String getDriverLicenceFrontImage() {
        return driverLicenceFrontImage;
    }

    public void setDriverLicenceFrontImage(String driverLicenceFrontImage) {
        this.driverLicenceFrontImage = driverLicenceFrontImage;
    }

    public String getDriverLicenceBackImage() {
        return driverLicenceBackImage;
    }

    public void setDriverLicenceBackImage(String driverLicenceBackImage) {
        this.driverLicenceBackImage = driverLicenceBackImage;
    }

    public String getPersonalPhone() {
        return personalPhone;
    }

    public void setPersonalPhone(String personalPhone) {
        this.personalPhone = personalPhone == null ? null : personalPhone.trim();
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone == null ? null : workPhone.trim();
    }

    public String getDriverLicenceFileNumber() {
        return driverLicenceFileNumber;
    }

    public void setDriverLicenceFileNumber(String driverLicenceFileNumber) {
        this.driverLicenceFileNumber = driverLicenceFileNumber;
    }

    public Integer getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(Integer operationStatus) {
        this.operationStatus = operationStatus;
    }

    public Integer getPeriodsStatus() {
        return periodsStatus;
    }

    public void setPeriodsStatus(Integer periodsStatus) {
        this.periodsStatus = periodsStatus;
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