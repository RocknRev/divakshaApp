package com.rak.divaksha.service.impl;

import com.rak.divaksha.entity.Sale;
import com.rak.divaksha.repository.SaleRepository;
import com.rak.divaksha.service.SaleService;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class SaleServiceImpl implements SaleService {

	private static final Logger logger = LoggerFactory.getLogger(SaleServiceImpl.class);

	private final SaleRepository saleRepository;
	private final UserServiceImpl userService;
	private final ReferralServiceImpl referralService;
	private final AffiliateServiceImpl affiliateService;

	public SaleServiceImpl(SaleRepository saleRepository, UserServiceImpl userService, ReferralServiceImpl referralService, AffiliateServiceImpl affiliateService) {
		this.saleRepository = saleRepository;
		this.userService = userService;
		this.referralService = referralService;
		this.affiliateService = affiliateService;
	}

	@Transactional
	@Override
	public Sale createSale(Long sellerId, Long buyerId, BigDecimal totalAmount) {
		return createSale(sellerId, buyerId, null, totalAmount);
	}

	@Transactional
	@Override
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

	@Override
	public Page<Sale> getAllSales(Pageable pageable) {
		return saleRepository.findAll(pageable);
	}

	@Override
	public Page<Sale> getSalesBySeller(Long sellerUserId, Pageable pageable) {
		return saleRepository.findBySellerUserId(sellerUserId, pageable);
	}
}

