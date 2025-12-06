package com.rak.divaksha.service;

import com.rak.divaksha.entity.CommissionLedger;
import com.rak.divaksha.entity.Sale;
import com.rak.divaksha.entity.User;
import com.rak.divaksha.repository.CommissionLedgerRepository;
import com.rak.divaksha.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReferralService {

	private static final Logger logger = LoggerFactory.getLogger(ReferralService.class);

	private final UserRepository userRepository;
	private final CommissionLedgerRepository commissionLedgerRepository;
	private final CommissionLevelService commissionLevelService;

	public ReferralService(UserRepository userRepository, CommissionLedgerRepository commissionLedgerRepository, CommissionLevelService commissionLevelService) {
		this.userRepository = userRepository;
		this.commissionLedgerRepository = commissionLedgerRepository;
		this.commissionLevelService = commissionLevelService;
	}

	@Transactional
	public List<CommissionLedger> calculateAndDistributeCommissions(Sale sale) {
		logger.info("Calculating commissions for sale ID: {}, amount: {}", sale.getId(), sale.getTotalAmount());

		List<CommissionLedger> ledgerEntries = new ArrayList<>();
		Long currentUserId = sale.getSellerUserId();
		BigDecimal saleAmount = sale.getTotalAmount();

		// Level 0: Seller gets 5%
		Optional<User> sellerOpt = userRepository.findByIdWithLock(currentUserId);
		if (sellerOpt.isPresent()) {
			BigDecimal sellerPercentage = commissionLevelService.getPercentageForLevel(0);
			User seller = sellerOpt.get();
			BigDecimal commissionAmount = calculateCommissionAmount(saleAmount, sellerPercentage);
			CommissionLedger ledger = new CommissionLedger(sale.getId(), seller.getId(), 0, sellerPercentage, commissionAmount, sale.getSellerUserId());
			ledgerEntries.add(commissionLedgerRepository.save(ledger));
			logger.info("Level 0 commission: {}% = {} for user {}", sellerPercentage, commissionAmount, seller.getId());
		}

		// Levels 1-10: Uplines
		for (int level = 1; level <= 10; level++) {
			if (currentUserId == null) {
				break;
			}

			Optional<User> currentUserOpt = userRepository.findByIdWithLock(currentUserId);
			if (currentUserOpt.isEmpty()) {
				break;
			}

			User currentUser = currentUserOpt.get();
			Long effectiveParentId = currentUser.getEffectiveParentId();

			if (effectiveParentId == null) {
				break;
			}

			Optional<User> parentOpt = userRepository.findByIdWithLock(effectiveParentId);
			if (parentOpt.isEmpty()) {
				break;
			}

			User parent = parentOpt.get();

			// Skip inactive uplines
			if (!parent.getIsActive()) {
				logger.info("Skipping level {} commission for inactive user {}", level, parent.getId());
				currentUserId = effectiveParentId;
				continue;
			}

			BigDecimal percentage = commissionLevelService.getPercentageForLevel(level);
			BigDecimal commissionAmount = calculateCommissionAmount(saleAmount, percentage);

			CommissionLedger ledger = new CommissionLedger(sale.getId(), parent.getId(), level, percentage, commissionAmount, sale.getSellerUserId());
			ledgerEntries.add(commissionLedgerRepository.save(ledger));
			logger.info("Level {} commission: {}% = {} for user {}", level, percentage, commissionAmount, parent.getId());

			currentUserId = effectiveParentId;
		}

		logger.info("Created {} commission ledger entries for sale ID: {}", ledgerEntries.size(), sale.getId());
		return ledgerEntries;
	}

	private BigDecimal calculateCommissionAmount(BigDecimal saleAmount, BigDecimal percentage) {
		return saleAmount.multiply(percentage)
				.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
	}
}

