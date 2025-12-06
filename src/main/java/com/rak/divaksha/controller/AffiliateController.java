package com.rak.divaksha.controller;

import com.rak.divaksha.dto.AffiliateLinkResponse;
import com.rak.divaksha.dto.AffiliateResponse;
import com.rak.divaksha.entity.User;
import com.rak.divaksha.repository.UserRepository;
import com.rak.divaksha.service.AffiliateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/aff")
public class AffiliateController {

	private final AffiliateService affiliateService;
	private final UserRepository userRepository;

	@Value("${baseUrl:http://localhost:3000}")
	private String frontendDomain;

	public AffiliateController(AffiliateService affiliateService, UserRepository userRepository) {
		this.affiliateService = affiliateService;
		this.userRepository = userRepository;
	}

	@GetMapping("/{code}")
	public ResponseEntity<AffiliateResponse> validateAffiliateCode(@PathVariable String code) {
		return affiliateService.validateAffiliateCode(code)
				.map(info -> {
					AffiliateResponse response = new AffiliateResponse();
					response.setAffiliateUserId(info.getAffiliateUserId());
					response.setAffiliateCode(info.getAffiliateCode());
					response.setUsername(info.getUsername());
					return ResponseEntity.ok(response);
				})
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/link/{userId}")
	public ResponseEntity<AffiliateLinkResponse> getAffiliateLink(@PathVariable Long userId) {
		try {
			Optional<User> userOpt = userRepository.findById(userId);
			if (userOpt.isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			User user = userOpt.get();
			if (user.getAffiliateCode() == null || user.getAffiliateCode().trim().isEmpty()) {
				return ResponseEntity.badRequest().build();
			}

			String affiliateLink = affiliateService.getAffiliateLink(userId, frontendDomain);
			
			AffiliateLinkResponse response = new AffiliateLinkResponse();
			response.setAffiliateLink(affiliateLink);
			response.setAffiliateCode(user.getAffiliateCode());

			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException | IllegalStateException e) {
			return ResponseEntity.badRequest().build();
		}
	}
}

