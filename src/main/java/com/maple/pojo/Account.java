package com.maple.pojo;

import java.util.Date;

public class Account {
    private Integer id;

    private Integer driverId;

    private String name;

    private Integer platform;

    private String account;

    private Date createTime;

    private Date updateTime;

    public Account(Integer id, Integer driverId, String name, Integer platform, String account, Date createTime, Date updateTime) {
        this.id = id;
        this.driverId = driverId;
        this.name = name;
        this.platform = platform;
        this.account = account;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Account() {
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

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
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