package com.rak.distribio.service;

import com.rak.distribio.entity.CommissionLedger;
import com.rak.distribio.entity.Sale;
import com.rak.distribio.entity.User;
import com.rak.distribio.repository.CommissionLedgerRepository;
import com.rak.distribio.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
public class AffiliateService {

	private static final Logger logger = LoggerFactory.getLogger(AffiliateService.class);

	private final UserRepository userRepository;
	private final CommissionLedgerRepository commissionLedgerRepository;
	private final CommissionLevelService commissionLevelService;

	public AffiliateService(UserRepository userRepository, CommissionLedgerRepository commissionLedgerRepository, CommissionLevelService commissionLevelService) {
		this.userRepository = userRepository;
		this.commissionLedgerRepository = commissionLedgerRepository;
		this.commissionLevelService = commissionLevelService;
	}

	/**
	 * Validate affiliate code and return affiliate user info
	 */
	public Optional<AffiliateInfo> validateAffiliateCode(String affiliateCode) {
		if (affiliateCode == null || affiliateCode.trim().isEmpty()) {
			return Optional.empty();
		}

		Optional<User> userOpt = userRepository.findByAffiliateCode(affiliateCode.trim());
		if (userOpt.isEmpty()) {
			logger.warn("Invalid affiliate code: {}", affiliateCode);
			return Optional.empty();
		}

		User user = userOpt.get();
		// Only return if user is active
		if (!user.getIsActive()) {
			logger.warn("Affiliate user {} is inactive", user.getId());
			return Optional.empty();
		}

		AffiliateInfo info = new AffiliateInfo();
		info.setAffiliateUserId(user.getId());
		info.setAffiliateCode(user.getAffiliateCode());
		info.setUsername(user.getUsername());

		return Optional.of(info);
	}

	/**
	 * Process affiliate commission for a sale (single-level only)
	 */
	@Transactional
	public void processAffiliateCommission(Sale sale) {
		if (sale.getAffiliateUserId() == null) {
			logger.debug("No affiliate user ID for sale {}, skipping affiliate commission", sale.getId());
			return;
		}

		logger.info("Processing affiliate commission for sale ID: {}, affiliateUserId: {}, amount: {}", 
				sale.getId(), sale.getAffiliateUserId(), sale.getTotalAmount());

		Optional<User> affiliateOpt = userRepository.findById(sale.getAffiliateUserId());
		if (affiliateOpt.isEmpty()) {
			logger.warn("Affiliate user {} not found for sale {}", sale.getAffiliateUserId(), sale.getId());
			return;
		}

		User affiliate = affiliateOpt.get();

		// Only pay commission if affiliate is active
		if (!affiliate.getIsActive()) {
			logger.info("Affiliate user {} is inactive, skipping commission", affiliate.getId());
			return;
		}
	
		// Direct affiliate commission percentage
		BigDecimal affCommisionPercentage = commissionLevelService.getPercentageForLevel(0);

		BigDecimal commissionAmount = calculateCommissionAmount(sale.getTotalAmount(), affCommisionPercentage);

		// Create commission ledger entry
		CommissionLedger ledger = new CommissionLedger(
				sale.getId(),
				affiliate.getId(),
				0, // Level 0 for affiliate (single-level)
				affCommisionPercentage,
				commissionAmount,
				sale.getSellerUserId()
		);

		commissionLedgerRepository.save(ledger);
		logger.info("Affiliate commission: {}% = {} for user {} on sale {}", affCommisionPercentage, commissionAmount, affiliate.getId(), sale.getId());
	}

	private BigDecimal calculateCommissionAmount(BigDecimal saleAmount, BigDecimal percentage) {
		return saleAmount.multiply(percentage)
				.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
	}

	/**
	 * Get affiliate link for a user
	 * @param userId The user ID
	 * @param frontendDomain The frontend domain (e.g., "https://myapp.com")
	 * @return The affiliate link
	 */
	public String getAffiliateLink(Long userId, String frontendDomain) {
		if(userId == null) throw new IllegalArgumentException("Invalid User");
		Optional<User> userOpt = userRepository.findById(userId);
		if (userOpt.isEmpty()) {
			throw new IllegalArgumentException("User not found");
		}

		User user = userOpt.get();
		if (user.getAffiliateCode() == null || user.getAffiliateCode().trim().isEmpty()) {
			throw new IllegalStateException("User does not have an affiliate code");
		}

		// Format: https://<frontend-domain>/aff/<affiliate_code>
		String baseUrl = frontendDomain.endsWith("/") 
			? frontendDomain.substring(0, frontendDomain.length() - 1) 
			: frontendDomain;
		
		return baseUrl + "/aff/" + user.getAffiliateCode();
	}

	/**
	 * Inner class for affiliate info response
	 */
	public static class AffiliateInfo {
		private Long affiliateUserId;
		private String affiliateCode;
		private String username;

		public Long getAffiliateUserId() {
			return affiliateUserId;
		}

		public void setAffiliateUserId(Long affiliateUserId) {
			this.affiliateUserId = affiliateUserId;
		}

		public String getAffiliateCode() {
			return affiliateCode;
		}

		public void setAffiliateCode(String affiliateCode) {
			this.affiliateCode = affiliateCode;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
	}
}

