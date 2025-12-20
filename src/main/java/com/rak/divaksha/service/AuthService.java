package com.rak.divaksha.service;

import java.util.Map;

public interface AuthService {

	public Map<String, Object> register(String username, String email, String password, String referralCode, String affiliateCode);

	public Map<String, Object> login(String email, String password);

	public String getReferralLink(Long userId);

	public String sendOtp(String email);

	public String verifyOtp(String email, String otp);

	public boolean isVerified(String email);

	public String resetPassword(String email, String newPassword);

    public String sendOtpForPasswordReset(String email);

}

