package com.rak.distribio.dto;

import java.time.OffsetDateTime;

public class UserResponse {

	private Long id;
	private String username;
	private String email;
	private Long parentId;
	private Long effectiveParentId;
	private Boolean isActive;
	private OffsetDateTime lastSaleAt;
	private OffsetDateTime inactiveSince;
	private OffsetDateTime createdAt;
	private String affiliateCode;
	private String role;

	public UserResponse() {
	}

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

	public String getAffiliateCode() {
		return affiliateCode;
	}

	public void setAffiliateCode(String affiliateCode) {
		this.affiliateCode = affiliateCode;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}

