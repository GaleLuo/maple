package com.maple.pojo;

import java.util.Date;

public class Ticket {
    private Integer id;

    private Integer carId;

    private Integer ticketTimes;

    private Integer score;

    private Integer money;

    private Date createTime;

    private Date updateTime;

    public Ticket(Integer id, Integer carId, Integer ticketTimes, Integer score, Integer money, Date createTime, Date updateTime) {
        this.id = id;
        this.carId = carId;
        this.ticketTimes = ticketTimes;
        this.score = score;
        this.money = money;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Ticket() {
        super();
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

    public Integer getTicketTimes() {
        return ticketTimes;
    }

    public void setTicketTimes(Integer ticketTimes) {
        this.ticketTimes = ticketTimes;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
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