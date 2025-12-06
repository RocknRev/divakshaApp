package com.rak.distribio.service;

import com.rak.distribio.dto.ReferralTreeNode;
import com.rak.distribio.entity.CommissionLedger;
import com.rak.distribio.entity.ReferralShiftHistory;
import com.rak.distribio.entity.User;
import com.rak.distribio.repository.CommissionLedgerRepository;
import com.rak.distribio.repository.ReferralShiftHistoryRepository;
import com.rak.distribio.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;
	private final ReferralShiftHistoryRepository referralShiftHistoryRepository;
	private final CommissionLedgerRepository commissionLedgerRepository;

	public UserService(UserRepository userRepository, ReferralShiftHistoryRepository referralShiftHistoryRepository, CommissionLedgerRepository commissionLedgerRepository) {
		this.userRepository = userRepository;
		this.referralShiftHistoryRepository = referralShiftHistoryRepository;
		this.commissionLedgerRepository = commissionLedgerRepository;
	}

	/**
	 * @deprecated Use AuthService.register() instead for proper authentication and referral code generation
	 */
	@Deprecated
	public User registerUser(String username, Long parentId) {
		throw new UnsupportedOperationException("Use AuthService.register() instead. This method is deprecated and no longer supported.");
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public Optional<User> getUserById(Long id) {
		if(id==null) throw new IllegalArgumentException("Invalid User ID");
		return userRepository.findById(id);
	}

	public Page<CommissionLedger> getLedgerCommissionByUser(Long userId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
		return commissionLedgerRepository.findByBeneficiaryUserId(userId, pageable);
	}

	public ReferralTreeNode buildTree(Long userId, int level) {
		if(userId==null) throw new IllegalArgumentException("Invalid User ID");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<User> children = userRepository.findByEffectiveParentId(userId);

        List<ReferralTreeNode> childNodes = children.stream()
                .map(child -> buildTree(child.getId(), level + 1))
                .collect(Collectors.toList());

        ReferralTreeNode node = new ReferralTreeNode();
        node.setUserId(user.getId());
        node.setUsername(user.getUsername());
        node.setActive(user.getIsActive());
        node.setLevel(level);
        node.setChildren(childNodes);

        return node;
    }

	@Transactional
	public void markUserInactive(Long userId) {
		logger.info("Marking user {} as inactive", userId);

		Optional<User> userOpt = userRepository.findByIdWithLock(userId);
		if (userOpt.isEmpty()) {
			throw new IllegalArgumentException("User not found: " + userId);
		}

		User user = userOpt.get();
		if (!user.getIsActive()) {
			logger.info("User {} is already inactive", userId);
			return;
		}

		user.setIsActive(false);
		user.setInactiveSince(OffsetDateTime.now());
		userRepository.save(user);

		// Shift all direct children to user's effective parent
		List<User> children = userRepository.findByEffectiveParentId(userId);
		Long newEffectiveParentId = user.getEffectiveParentId();

		for (User child : children) {
			Long previousEffectiveParentId = child.getEffectiveParentId();
			child.setEffectiveParentId(newEffectiveParentId);
			userRepository.save(child);

			// Create history entry
			ReferralShiftHistory history = new ReferralShiftHistory(
				child.getId(),
				userId,
				previousEffectiveParentId,
				newEffectiveParentId
			);
			referralShiftHistoryRepository.save(history);
			logger.info("Shifted child {} from parent {} to parent {}", child.getId(), previousEffectiveParentId, newEffectiveParentId);
		}

		logger.info("User {} marked as inactive and {} children shifted", userId, children.size());
	}

	@Transactional
	public void reactivateUser(Long userId) {
		logger.info("Reactivating user {}", userId);

		Optional<User> userOpt = userRepository.findByIdWithLock(userId);
		if (userOpt.isEmpty()) {
			throw new IllegalArgumentException("User not found: " + userId);
		}

		User user = userOpt.get();
		if (user.getIsActive()) {
			logger.info("User {} is already active", userId);
			return;
		}

		// Revert all referral shift history entries for this user
		List<ReferralShiftHistory> historyEntries = referralShiftHistoryRepository.findByInactiveUserIdAndRevertedFalse(userId);

		for (ReferralShiftHistory history : historyEntries) {
			Optional<User> childOpt = userRepository.findByIdWithLock(history.getAffectedChildId());
			if (childOpt.isPresent()) {
				User child = childOpt.get();
				child.setEffectiveParentId(history.getPreviousEffectiveParentId());
				userRepository.save(child);
				logger.info("Reverted child {} back to parent {}", child.getId(), history.getPreviousEffectiveParentId());
			}

			history.setReverted(true);
			history.setRevertAt(OffsetDateTime.now());
			referralShiftHistoryRepository.save(history);
		}

		user.setIsActive(true);
		user.setInactiveSince(null);
		userRepository.save(user);

		logger.info("User {} reactivated and {} history entries reverted", userId, historyEntries.size());
	}

	@Transactional
	public void updateLastSaleAt(Long userId) {
		Optional<User> userOpt = userRepository.findByIdWithLock(userId);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			user.setLastSaleAt(OffsetDateTime.now());

			// If user was inactive, reactivate them
			if (!user.getIsActive()) {
				logger.info("User {} made a sale while inactive, reactivating", userId);
				
				// Revert all referral shift history entries for this user
				List<ReferralShiftHistory> historyEntries = referralShiftHistoryRepository.findByInactiveUserIdAndRevertedFalse(userId);

				for (ReferralShiftHistory history : historyEntries) {
					Optional<User> childOpt = userRepository.findByIdWithLock(history.getAffectedChildId());
					if (childOpt.isPresent()) {
						User child = childOpt.get();
						child.setEffectiveParentId(history.getPreviousEffectiveParentId());
						userRepository.save(child);
						logger.info("Reverted child {} back to parent {}", child.getId(), history.getPreviousEffectiveParentId());
					}

					history.setReverted(true);
					history.setRevertAt(OffsetDateTime.now());
					referralShiftHistoryRepository.save(history);
				}

				user.setIsActive(true);
				user.setInactiveSince(null);
				userRepository.save(user);
				logger.info("User {} reactivated and {} history entries reverted", userId, historyEntries.size());
			} else {
				userRepository.save(user);
			}
		}
	}
}

