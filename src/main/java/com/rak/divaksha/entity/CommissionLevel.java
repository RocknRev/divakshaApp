package com.rak.divaksha.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "commission_levels")
public class CommissionLevel {
    @Id
    private Integer level; // 0 = seller, 1-10 = uplines

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal percentage;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    
}
