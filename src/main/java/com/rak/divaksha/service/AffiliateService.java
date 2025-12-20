package com.rak.divaksha.service;

import com.rak.divaksha.entity.Sale;
import com.rak.divaksha.service.impl.AffiliateServiceImpl.AffiliateInfo;
import java.util.Optional;

public interface AffiliateService {

	public Optional<AffiliateInfo> validateAffiliateCode(String affiliateCode);

	public void processAffiliateCommission(Sale sale);

	public String getAffiliateLink(Long userId, String frontendDomain);

}
