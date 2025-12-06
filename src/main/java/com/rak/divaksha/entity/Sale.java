package com.rak.divaksha.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "sales")
public class Sale {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "seller_user_id")
	private Long sellerUserId;

	@Column(name = "buyer_id")
	private Long buyerId;

	@Column(name = "affiliate_user_id")
	private Long affiliateUserId;

	@Column(name = "total_amount", precision = 18, scale = 2)
	private BigDecimal totalAmount;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private OffsetDateTime createdAt;

	// Constructors
	public Sale() {
	}

	public Sale(Long sellerUserId, Long buyerId, BigDecimal totalAmount) {
		this.sellerUserId = sellerUserId;
		this.buyerId = buyerId;
		this.totalAmount = totalAmount;
	}

	public Sale(Long sellerUserId, Long buyerId, Long affiliateUserId, BigDecimal totalAmount) {
		this.sellerUserId = sellerUserId;
		this.buyerId = buyerId;
		this.affiliateUserId = affiliateUserId;
		this.totalAmount = totalAmount;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSellerUserId() {
		return sellerUserId;
	}

	public void setSellerUserId(Long sellerUserId) {
		this.sellerUserId = sellerUserId;
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

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}
}

