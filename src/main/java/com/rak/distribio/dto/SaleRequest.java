package com.rak.distribio.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class SaleRequest {

	@NotNull(message = "Seller ID is required")
	private Long sellerId;

	private Long buyerId;

	private Long affiliateUserId;

	@NotNull(message = "Total amount is required")
	@Positive(message = "Total amount must be positive")
	private BigDecimal totalAmount;

	// Constructors
	public SaleRequest() {
	}

	public SaleRequest(Long sellerId, Long buyerId, BigDecimal totalAmount) {
		this.sellerId = sellerId;
		this.buyerId = buyerId;
		this.totalAmount = totalAmount;
	}

	public SaleRequest(Long sellerId, Long buyerId, Long affiliateUserId, BigDecimal totalAmount) {
		this.sellerId = sellerId;
		this.buyerId = buyerId;
		this.affiliateUserId = affiliateUserId;
		this.totalAmount = totalAmount;
	}

	// Getters and Setters
	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	public Long getAffiliateUserId() {
		return affiliateUserId;
	}

	public void setAffiliateUserId(Long affiliateUserId) {
		this.affiliateUserId = affiliateUserId;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
}

