package com.rak.distribio.controller;

import com.rak.distribio.dto.AuthResponse;
import com.rak.distribio.dto.LoginRequest;
import com.rak.distribio.dto.RegisterRequest;
import com.rak.distribio.service.AffiliateService;
import com.rak.distribio.service.AuthService;
import jakarta.validation.Valid;
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
			var result = authService.register(
					request.getUsername(),
					request.getEmail(),
					request.getPassword(),
					request.getReferralCode(),
					request.getAffiliateCode()
			);

			com.rak.distribio.entity.User user = (com.rak.distribio.entity.User) result.get("user");
			com.rak.distribio.dto.UserResponse userResponse = new com.rak.distribio.dto.UserResponse();
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

			com.rak.distribio.entity.User user = (com.rak.distribio.entity.User) result.get("user");
			com.rak.distribio.dto.UserResponse userResponse = new com.rak.distribio.dto.UserResponse();
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
}

