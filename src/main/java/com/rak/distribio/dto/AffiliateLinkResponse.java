package com.rak.distribio.dto;

public class AffiliateLinkResponse {

	private String affiliateLink;
	private String affiliateCode;

	// Constructors
	public AffiliateLinkResponse() {
	}

	public AffiliateLinkResponse(String affiliateLink, String affiliateCode) {
		this.affiliateLink = affiliateLink;
		this.affiliateCode = affiliateCode;
	}

	// Getters and Setters
	public String getAffiliateLink() {
		return affiliateLink;
	}

	public void setAffiliateLink(String affiliateLink) {
		this.affiliateLink = affiliateLink;
	}

	public String getAffiliateCode() {
		return affiliateCode;
	}

	public void setAffiliateCode(String affiliateCode) {
		this.affiliateCode = affiliateCode;
	}
}

