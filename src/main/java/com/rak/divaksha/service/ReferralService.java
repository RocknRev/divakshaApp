package com.rak.divaksha.service;

import com.rak.divaksha.entity.CommissionLedger;
import com.rak.divaksha.entity.Sale;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ReferralService {

	public List<CommissionLedger> calculateAndDistributeCommissions(Sale sale);
	
}

