package com.suse.hwj.springboot.entity;

import java.math.BigDecimal;

public class RechargeDTO {
    private BigDecimal amount;
    private String name;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
