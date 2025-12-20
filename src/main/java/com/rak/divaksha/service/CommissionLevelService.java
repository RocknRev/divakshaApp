package com.rak.divaksha.service;

import java.math.BigDecimal;

public interface CommissionLevelService {
    public BigDecimal getPercentageForLevel(int level);
}
