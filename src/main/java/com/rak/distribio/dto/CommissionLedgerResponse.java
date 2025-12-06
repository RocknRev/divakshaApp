package com.rak.distribio.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class CommissionLedgerResponse {

	private Long id;
	private Long saleId;
	private Long beneficiaryUserId;
	private Integer level;
	private BigDecimal percentage;
	private BigDecimal amount;
	private OffsetDateTime createdAt;

	// Constructors
	public CommissionLedgerResponse() {
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}

	public Long getBeneficiaryUserId() {
		return beneficiaryUserId;
	}

	public void setBeneficiaryUserId(Long beneficiaryUserId) {
		this.beneficiaryUserId = beneficiaryUserId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public BigDecimal getPercentage() {
		return percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}
}

