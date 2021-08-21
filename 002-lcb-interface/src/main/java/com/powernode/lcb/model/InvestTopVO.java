package com.powernode.lcb.model;

import java.io.Serializable;

public class InvestTopVO implements Serializable {
    private String phone;
    private double money;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
