package com.rak.divaksha.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.rak.divaksha.entity.CommissionLevel;
import com.rak.divaksha.repository.CommissionLevelRepository;

@Service
public class CommissionLevelService {

    private final CommissionLevelRepository repository;
    private Map<Integer, BigDecimal> commissionMap;

    public CommissionLevelService(CommissionLevelRepository repository) {
        this.repository = repository;
        loadCommissionPercentages();
    }

    private void loadCommissionPercentages() {
        commissionMap = repository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        CommissionLevel::getLevel,
                        CommissionLevel::getPercentage
                ));
    }

    public BigDecimal getPercentageForLevel(int level) {
        return commissionMap.getOrDefault(level, BigDecimal.ZERO);
    }
}
