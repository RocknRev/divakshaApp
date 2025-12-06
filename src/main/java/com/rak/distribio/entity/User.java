package com.rak.distribio.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.OffsetDateTime;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(name = "referral_code", unique = true, nullable = false)
	private String referralCode;

	@Column(name = "affiliate_code", unique = true, nullable = false)
	private String affiliateCode;

	@Column(name = "parent_id")
	private Long parentId;

	@Column(name = "effective_parent_id")
	private Long effectiveParentId;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive = true;

	@Column(name = "last_sale_at")
	private OffsetDateTime lastSaleAt;

	@Column(name = "inactive_since")
	private OffsetDateTime inactiveSince;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private OffsetDateTime createdAt;

	@Column(name = "role")
	private String role;

	// Constructors
	public User() {
	}

	public User(String username, String email, String password, String referralCode, String affiliateCode, Long parentId, String role) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.referralCode = referralCode;
		this.affiliateCode = affiliateCode;
		this.parentId = parentId;
		this.effectiveParentId = parentId;
		this.isActive = true;
		this.role = role;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getReferralCode() {
		return referralCode;
	}

	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}

	public String getAffiliateCode() {
		return affiliateCode;
	}

	public void setAffiliateCode(String affiliateCode) {
		this.affiliateCode = affiliateCode;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getEffectiveParentId() {
		return effectiveParentId;
	}

	public void setEffectiveParentId(Long effectiveParentId) {
		this.effectiveParentId = effectiveParentId;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public OffsetDateTime getLastSaleAt() {
		return lastSaleAt;
	}

	public void setLastSaleAt(OffsetDateTime lastSaleAt) {
		this.lastSaleAt = lastSaleAt;
	}

	public OffsetDateTime getInactiveSince() {
		return inactiveSince;
	}

	public void setInactiveSince(OffsetDateTime inactiveSince) {
		this.inactiveSince = inactiveSince;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}

