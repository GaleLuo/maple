package com.maple.jo;

/**
 * Created by Maple.Ran on 2017/9/23.
 */
public class TicketJo {
    private String date;//日期
    private String area;//地点
    private String act;//行为描述
    private String code;
    private Integer fen;//扣分
    private Integer money;//扣钱
    private Integer handled;//处理

    @Override
    public String toString() {
        return "TicketJo{" +
                "date='" + date + '\'' +
                ", area='" + area + '\'' +
                ", act='" + act + '\'' +
                ", code='" + code + '\'' +
                ", fen=" + fen +
                ", money=" + money +
                ", handle=" + handled +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getFen() {
        return fen;
    }

    public void setFen(Integer fen) {
        this.fen = fen;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getHandled() {
        return handled;
    }

    public void setHandled(Integer handled) {
        this.handled = handled;
    }
}
