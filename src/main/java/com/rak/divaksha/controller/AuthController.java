package com.rak.divaksha.controller;

import com.rak.divaksha.dto.AuthResponse;
import com.rak.divaksha.dto.LoginRequest;
import com.rak.divaksha.dto.RegisterRequest;
import com.rak.divaksha.service.AffiliateService;
import com.rak.divaksha.service.AuthService;
import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;
	private final AffiliateService affiliateService;

	@Value("${app.frontend.domain:http://localhost:3000}")
	private String frontendDomain;

	public AuthController(AuthService authService, AffiliateService affiliateService) {
		this.authService = authService;
		this.affiliateService = affiliateService;
	}

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
		try {
			if (!authService.isVerified(request.getEmail())) {
				return ResponseEntity.badRequest().body(new AuthResponse("Email OTP Not Verified!"));
			}
			var result = authService.register(
					request.getUsername(),
					request.getEmail(),
					request.getPassword(),
					request.getReferralCode(),
					request.getAffiliateCode()
			);

			com.rak.divaksha.entity.User user = (com.rak.divaksha.entity.User) result.get("user");
			com.rak.divaksha.dto.UserResponse userResponse = new com.rak.divaksha.dto.UserResponse();
			userResponse.setId(user.getId());
			userResponse.setUsername(user.getUsername());
			userResponse.setEmail(user.getEmail());
			userResponse.setParentId(user.getParentId());
			userResponse.setEffectiveParentId(user.getEffectiveParentId());
			userResponse.setIsActive(user.getIsActive());
			userResponse.setLastSaleAt(user.getLastSaleAt());
			userResponse.setInactiveSince(user.getInactiveSince());
			userResponse.setCreatedAt(user.getCreatedAt());
			userResponse.setRole(user.getRole());

			AuthResponse response = new AuthResponse();
			response.setToken((String) result.get("token"));
			response.setUser(userResponse);
			response.setReferralCode((String) result.get("referralCode"));
			response.setReferralLink(authService.getReferralLink(user.getId()));
			response.setAffiliateCode(user.getAffiliateCode());
			response.setAffiliateLink(affiliateService.getAffiliateLink(user.getId(), frontendDomain));

			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
		try {
			var result = authService.login(request.getEmail(), request.getPassword());

			com.rak.divaksha.entity.User user = (com.rak.divaksha.entity.User) result.get("user");
			com.rak.divaksha.dto.UserResponse userResponse = new com.rak.divaksha.dto.UserResponse();
			userResponse.setId(user.getId());
			userResponse.setUsername(user.getUsername());
			userResponse.setEmail(user.getEmail());
			userResponse.setParentId(user.getParentId());
			userResponse.setEffectiveParentId(user.getEffectiveParentId());
			userResponse.setIsActive(user.getIsActive());
			userResponse.setLastSaleAt(user.getLastSaleAt());
			userResponse.setInactiveSince(user.getInactiveSince());
			userResponse.setCreatedAt(user.getCreatedAt());
			userResponse.setRole(user.getRole());

			AuthResponse response = new AuthResponse();
			response.setToken((String) result.get("token"));
			response.setUser(userResponse);
			response.setReferralCode((String) result.get("referralCode"));
			response.setReferralLink(authService.getReferralLink(user.getId()));
			response.setAffiliateCode(user.getAffiliateCode());
			response.setAffiliateLink(affiliateService.getAffiliateLink(user.getId(), frontendDomain));

			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@GetMapping("/referral-link/{userId}")
	public ResponseEntity<String> getReferralLink(@PathVariable Long userId) {
		try {
			String referralLink = authService.getReferralLink(userId);
			return ResponseEntity.ok(referralLink);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/send-email-otp")
    public ResponseEntity<String> sendEmailOtp(@RequestBody Map<String, String> req) {
        authService.sendOtp(req.get("email"));
        return ResponseEntity.ok("OTP sent");
    }

	@PostMapping("/verify-email-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody Map<String, String> req) {
        boolean result = authService.verifyOtp(req.get("email"), req.get("otp"));
        return ResponseEntity.ok(Map.of("verified", result));
    }

}

