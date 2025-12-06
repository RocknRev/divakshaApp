package com.rak.divaksha.dto;

public class AffiliateResponse {

	private Long affiliateUserId;
	private String affiliateCode;
	private String username;

	// Constructors
	public AffiliateResponse() {
	}

	public AffiliateResponse(Long affiliateUserId, String affiliateCode, String username) {
		this.affiliateUserId = affiliateUserId;
		this.affiliateCode = affiliateCode;
		this.username = username;
	}

	// Getters and Setters
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

