package com.rak.distribio.service;

import com.rak.distribio.entity.User;
import com.rak.distribio.repository.UserRepository;
import com.rak.distribio.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
	private static final String REFERRAL_CODE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final int REFERRAL_CODE_LENGTH = 8;
	private static final int AFFILIATE_CODE_LENGTH = 10;

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	@Transactional
	public Map<String, Object> register(String username, String email, String password, String referralCode, String affiliateCode) {
		logger.info("Registering new user: {}", username);

		// Validate email format
		if (!isValidEmail(email)) {
			throw new IllegalArgumentException("Invalid email format");
		}

		// Check if username already exists
		if (userRepository.findByUsername(username).isPresent()) {
			throw new IllegalArgumentException("Username already exists");
		}

		// Check if email already exists
		if (userRepository.findByEmail(email).isPresent()) {
			throw new IllegalArgumentException("Email already exists");
		}

		// Find parent by referral code if provided
		Long parentId = null;
		if (referralCode != null && !referralCode.trim().isEmpty()) {
			Optional<User> parentOpt = userRepository.findByReferralCode(referralCode);
			if (parentOpt.isEmpty()) {
				throw new IllegalArgumentException("Invalid referral code");
			}
			parentId = parentOpt.get().getId();
		}

		// If affiliate code is provided and no referral code, use affiliate as parent
		// This handles the case where visitor came via affiliate link and then registered
		if (parentId == null && affiliateCode != null && !affiliateCode.trim().isEmpty()) {
			Optional<User> affiliateOpt = userRepository.findByAffiliateCode(affiliateCode);
			if (affiliateOpt.isPresent()) {
				User affiliate = affiliateOpt.get();
				if (affiliate.getIsActive()) {
					parentId = affiliate.getId();
					logger.info("Setting affiliate {} as parent for new user {}", affiliate.getId(), username);
				}
			}
		}

		// Generate unique referral code and affiliate code for new user
		String newReferralCode = generateUniqueReferralCode();
		String newAffiliateCode = generateUniqueAffiliateCode();

		// Hash password
		String hashedPassword = passwordEncoder.encode(password);

		// Create user
		User user = new User(username, email, hashedPassword, newReferralCode, newAffiliateCode, parentId, "USER");
		
		// Set effective parent based on parent's active status
		if (parentId != null) {
			Optional<User> parentOpt = userRepository.findById(parentId);
			if (parentOpt.isPresent()) {
				User parent = parentOpt.get();
				if (parent.getIsActive()) {
					user.setEffectiveParentId(parentId);
				} else {
					user.setEffectiveParentId(parent.getEffectiveParentId());
				}
			} else {
				user.setEffectiveParentId(null);
			}
		} else {
			user.setEffectiveParentId(null);
		}

		user = userRepository.save(user);

		// Generate JWT token
		String token = jwtUtil.generateToken(user.getUsername(), user.getId());

		Map<String, Object> response = new HashMap<>();
		response.put("token", token);
		response.put("user", user);
		response.put("affiliateCode", newAffiliateCode);

		logger.info("User registered successfully: {}", username);
		return response;
	}

	public Map<String, Object> login(String email, String password) {
		logger.info("Login attempt for email: {}", email);

		Optional<User> userOpt = userRepository.findByEmail(email);
		if (userOpt.isEmpty()) {
			throw new IllegalArgumentException("Invalid email or password");
		}

		User user = userOpt.get();

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new IllegalArgumentException("Invalid email or password");
		}

		// Generate JWT token
		String token = jwtUtil.generateToken(user.getUsername(), user.getId());

		Map<String, Object> response = new HashMap<>();
		response.put("token", token);
		response.put("user", user);
		response.put("referralCode", user.getReferralCode());

		logger.info("User logged in successfully: {}", user.getUsername());
		return response;
	}

	public String getReferralLink(Long userId) {
		Optional<User> userOpt = userRepository.findById(userId);
		if (userOpt.isEmpty()) {
			throw new IllegalArgumentException("User not found");
		}

		User user = userOpt.get();
		return "http://localhost:3000/register?ref=" + user.getReferralCode();
	}

	private String generateUniqueReferralCode() {
		SecureRandom random = new SecureRandom();
		String code;
		do {
			StringBuilder sb = new StringBuilder(REFERRAL_CODE_LENGTH);
			for (int i = 0; i < REFERRAL_CODE_LENGTH; i++) {
				sb.append(REFERRAL_CODE_CHARS.charAt(random.nextInt(REFERRAL_CODE_CHARS.length())));
			}
			code = sb.toString();
		} while (userRepository.findByReferralCode(code).isPresent());

		return code;
	}

	private String generateUniqueAffiliateCode() {
		SecureRandom random = new SecureRandom();
		String code;
		do {
			StringBuilder sb = new StringBuilder(AFFILIATE_CODE_LENGTH);
			for (int i = 0; i < AFFILIATE_CODE_LENGTH; i++) {
				sb.append(REFERRAL_CODE_CHARS.charAt(random.nextInt(REFERRAL_CODE_CHARS.length())));
			}
			code = sb.toString();
		} while (userRepository.findByAffiliateCode(code).isPresent());

		return code;
	}

	private boolean isValidEmail(String email) {
		return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
	}
}

