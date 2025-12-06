package com.rak.distribio.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "commission_ledger")
public class CommissionLedger {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "sale_id")
	private Long saleId;

	@Column(name = "beneficiary_user_id")
	private Long beneficiaryUserId;

	@Column(name = "level")
	private Integer level;

	@Column(name = "percentage", precision = 5, scale = 2)
	private BigDecimal percentage;

	@Column(name = "amount", precision = 18, scale = 2)
	private BigDecimal amount;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private OffsetDateTime createdAt;

	@Column(name="seller_user_id", nullable = false)
	private Long sellerUserId;

	// Constructors
	public CommissionLedger() {
	}

	public CommissionLedger(Long saleId, Long beneficiaryUserId, Integer level, BigDecimal percentage, BigDecimal amount, Long sellerUserId) {
		this.saleId = saleId;
		this.beneficiaryUserId = beneficiaryUserId;
		this.level = level;
		this.percentage = percentage;
		this.amount = amount;
		this.sellerUserId = sellerUserId;
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

	public Long getSellerUserId() {
		return sellerUserId;
	}

	public void setSellerUserId(Long sellerUserId) {
		this.sellerUserId = sellerUserId;
	}
}

