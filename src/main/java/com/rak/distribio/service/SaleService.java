package com.rak.distribio.service;

import com.rak.distribio.entity.Sale;
import com.rak.distribio.repository.SaleRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class SaleService {

	private static final Logger logger = LoggerFactory.getLogger(SaleService.class);

	private final SaleRepository saleRepository;
	private final UserService userService;
	private final ReferralService referralService;
	private final AffiliateService affiliateService;

	public SaleService(SaleRepository saleRepository, UserService userService, ReferralService referralService, AffiliateService affiliateService) {
		this.saleRepository = saleRepository;
		this.userService = userService;
		this.referralService = referralService;
		this.affiliateService = affiliateService;
	}

	@Transactional
	public Sale createSale(Long sellerId, Long buyerId, BigDecimal totalAmount) {
		return createSale(sellerId, buyerId, null, totalAmount);
	}

	@Transactional
	public Sale createSale(Long sellerId, Long buyerId, Long affiliateUserId, BigDecimal totalAmount) {
		logger.info("Creating sale: sellerId={}, buyerId={}, affiliateUserId={}, amount={}", 
				sellerId, buyerId, affiliateUserId, totalAmount);

		// Lock seller row and reactivate if inactive
		userService.updateLastSaleAt(sellerId);

		// Create sale with affiliate user ID if provided
		Sale sale;
		if (affiliateUserId != null) {
			sale = new Sale(sellerId, buyerId, affiliateUserId, totalAmount);
		} else {
			sale = new Sale(sellerId, buyerId, totalAmount);
		}
		sale = saleRepository.save(sale);

		// Process affiliate commission (single-level, if affiliateUserId is present)
		if (affiliateUserId != null) {
			affiliateService.processAffiliateCommission(sale);
		}

		// Calculate and distribute referral commissions (multi-level)
		referralService.calculateAndDistributeCommissions(sale);

		logger.info("Sale created: ID={}", sale.getId());
		return sale;
	}

	public Page<Sale> getAllSales(Pageable pageable) {
		return saleRepository.findAll(pageable);
	}

	public Page<Sale> getSalesBySeller(Long sellerUserId, Pageable pageable) {
		return saleRepository.findBySellerUserId(sellerUserId, pageable);
	}
}

