package com.rak.distribio.dto;

import com.rak.distribio.entity.User;

public class AuthResponse {

	private String token;
	private UserResponse user;
	private String referralCode;
	private String referralLink;
	private String affiliateCode;
	private String affiliateLink;

	// Constructors
	public AuthResponse() {
	}

	public AuthResponse(String token, User user, String referralCode, String referralLink) {
		this.token = token;
		this.user = mapUserToResponse(user);
		this.referralCode = referralCode;
		this.referralLink = referralLink;
	}

	// Getters and Setters
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserResponse getUser() {
		return user;
	}

	public void setUser(UserResponse user) {
		this.user = user;
	}

	public String getReferralCode() {
		return referralCode;
	}

	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}

	public String getReferralLink() {
		return referralLink;
	}

	public void setReferralLink(String referralLink) {
		this.referralLink = referralLink;
	}

	public String getAffiliateCode() {
		return affiliateCode;
	}

	public void setAffiliateCode(String affiliateCode) {
		this.affiliateCode = affiliateCode;
	}

	public String getAffiliateLink() {
		return affiliateLink;
	}

	public void setAffiliateLink(String affiliateLink) {
		this.affiliateLink = affiliateLink;
	}

	private UserResponse mapUserToResponse(User user) {
		if (user == null) {
			return null;
		}
		UserResponse response = new UserResponse();
		response.setId(user.getId());
		response.setUsername(user.getUsername());
		response.setEmail(user.getEmail());
		response.setParentId(user.getParentId());
		response.setEffectiveParentId(user.getEffectiveParentId());
		response.setIsActive(user.getIsActive());
		response.setLastSaleAt(user.getLastSaleAt());
		response.setInactiveSince(user.getInactiveSince());
		response.setCreatedAt(user.getCreatedAt());
		return response;
	}
}

