package com.rak.divaksha.controller;

import com.rak.divaksha.dto.PagedResponse;
import com.rak.divaksha.dto.ReferralTreeNode;
import com.rak.divaksha.dto.UserResponse;
import com.rak.divaksha.entity.CommissionLedger;
import com.rak.divaksha.entity.User;
import com.rak.divaksha.service.UserService;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		List<User> users = userService.getAllUsers();
		List<UserResponse> responses = users.stream()
			.map(this::mapToResponse)
			.collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
		return userService.getUserById(id)
			.map(user -> ResponseEntity.ok(mapToResponse(user)))
			.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/tree/{userId}")
    public ReferralTreeNode getReferralTree(@PathVariable Long userId) {
        return userService.buildTree(userId, 0);
    }

	@GetMapping("/ledger/{userId}")
	public ResponseEntity<PagedResponse<CommissionLedger>> getLedgerCommissionByUser(@PathVariable Long userId,
			@RequestParam int page, @RequestParam int size) {

		Page<CommissionLedger> ledgerPage = userService.getLedgerCommissionByUser(userId, page, size);
		return ResponseEntity.ok(new PagedResponse<>(ledgerPage));
	}


	@PostMapping("/admin/deactivate/{userId}")
	public ResponseEntity<Void> deactivateUser(@PathVariable Long userId) {
		try {
			userService.markUserInactive(userId);
			return ResponseEntity.ok().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/admin/activate/{userId}")
	public ResponseEntity<Void> activateUser(@PathVariable Long userId) {
		try {
			userService.reactivateUser(userId);
			return ResponseEntity.ok().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	private UserResponse mapToResponse(User user) {
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
		response.setAffiliateCode(user.getAffiliateCode());
		response.setRole(user.getRole());
		return response;
	}

}

