package com.maple.pojo;

import java.util.Date;

public class ContactInfo {
    private Integer id;

    private Integer driverId;

    private String wechat;

    private String email;

    private String mailAdd;

    private String livingAdd;

    private String emNameFirst;

    private String emPhoneFirst;

    private String emAddFirst;

    private String emNameSecond;

    private String emPhoneSecond;

    private String emAddSecond;

    private Date createTime;

    private Date updateTime;

    public ContactInfo(Integer id, Integer driverId, String wechat, String email, String mailAdd, String livingAdd, String emNameFirst, String emPhoneFirst, String emAddFirst, String emNameSecond, String emPhoneSecond, String emAddSecond, Date createTime, Date updateTime) {
        this.id = id;
        this.driverId = driverId;
        this.wechat = wechat;
        this.email = email;
        this.mailAdd = mailAdd;
        this.livingAdd = livingAdd;
        this.emNameFirst = emNameFirst;
        this.emPhoneFirst = emPhoneFirst;
        this.emAddFirst = emAddFirst;
        this.emNameSecond = emNameSecond;
        this.emPhoneSecond = emPhoneSecond;
        this.emAddSecond = emAddSecond;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public ContactInfo() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat == null ? null : wechat.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getMailAdd() {
        return mailAdd;
    }

    public void setMailAdd(String mailAdd) {
        this.mailAdd = mailAdd == null ? null : mailAdd.trim();
    }

    public String getLivingAdd() {
        return livingAdd;
    }

    public void setLivingAdd(String livingAdd) {
        this.livingAdd = livingAdd == null ? null : livingAdd.trim();
    }

    public String getEmNameFirst() {
        return emNameFirst;
    }

    public void setEmNameFirst(String emNameFirst) {
        this.emNameFirst = emNameFirst == null ? null : emNameFirst.trim();
    }

    public String getEmPhoneFirst() {
        return emPhoneFirst;
    }

    public void setEmPhoneFirst(String emPhoneFirst) {
        this.emPhoneFirst = emPhoneFirst == null ? null : emPhoneFirst.trim();
    }

    public String getEmAddFirst() {
        return emAddFirst;
    }

    public void setEmAddFirst(String emAddFirst) {
        this.emAddFirst = emAddFirst == null ? null : emAddFirst.trim();
    }

    public String getEmNameSecond() {
        return emNameSecond;
    }

    public void setEmNameSecond(String emNameSecond) {
        this.emNameSecond = emNameSecond == null ? null : emNameSecond.trim();
    }

    public String getEmPhoneSecond() {
        return emPhoneSecond;
    }

    public void setEmPhoneSecond(String emPhoneSecond) {
        this.emPhoneSecond = emPhoneSecond == null ? null : emPhoneSecond.trim();
    }

    public String getEmAddSecond() {
        return emAddSecond;
    }

    public void setEmAddSecond(String emAddSecond) {
        this.emAddSecond = emAddSecond == null ? null : emAddSecond.trim();
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