package com.suse.hwj.springboot.entity;

public class RechargeDTO {
    private Integer amount;
    private String name;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
