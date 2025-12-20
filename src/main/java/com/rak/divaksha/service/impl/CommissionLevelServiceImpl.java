package com.rak.divaksha.service.impl;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.rak.divaksha.entity.CommissionLevel;
import com.rak.divaksha.repository.CommissionLevelRepository;
import com.rak.divaksha.service.CommissionLevelService;

@Service
public class CommissionLevelServiceImpl implements CommissionLevelService {

    private final CommissionLevelRepository repository;
    private Map<Integer, BigDecimal> commissionMap;

    public CommissionLevelServiceImpl(CommissionLevelRepository repository) {
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

    @Override
    public BigDecimal getPercentageForLevel(int level) {
        return commissionMap.getOrDefault(level, BigDecimal.ZERO);
    }
}
