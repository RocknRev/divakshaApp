package com.rak.distribio.service;

import com.rak.distribio.entity.ReferralShiftHistory;
import com.rak.distribio.entity.User;
import com.rak.distribio.repository.ReferralShiftHistoryRepository;
import com.rak.distribio.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private ReferralShiftHistoryRepository referralShiftHistoryRepository;

	@InjectMocks
	private UserService userService;

	private User parentUser;
	private User activeUser;
	private User childUser1;
	private User childUser2;

	@BeforeEach
	void setUp() {
		parentUser = new User();
		parentUser.setId(1L);
		parentUser.setUsername("parent");
		parentUser.setIsActive(true);
		parentUser.setEffectiveParentId(null);

		activeUser = new User();
		activeUser.setId(2L);
		activeUser.setUsername("active");
		activeUser.setIsActive(true);
		activeUser.setEffectiveParentId(1L);
		activeUser.setLastSaleAt(OffsetDateTime.now().minusWeeks(1));

		childUser1 = new User();
		childUser1.setId(3L);
		childUser1.setUsername("child1");
		childUser1.setIsActive(true);
		childUser1.setEffectiveParentId(2L);

		childUser2 = new User();
		childUser2.setId(4L);
		childUser2.setUsername("child2");
		childUser2.setIsActive(true);
		childUser2.setEffectiveParentId(2L);
	}

	@Test
	void testRegisterUser_WithParent() {
		when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
		when(userRepository.findById(1L)).thenReturn(Optional.of(parentUser));
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

		User result = userService.registerUser("newuser", 1L);

		assertNotNull(result);
		assertEquals("newuser", result.getUsername());
		assertEquals(1L, result.getParentId());
		assertEquals(1L, result.getEffectiveParentId());
		assertTrue(result.getIsActive());
		verify(userRepository).save(any(User.class));
	}

	@Test
	void testRegisterUser_WithoutParent() {
		when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

		User result = userService.registerUser("newuser", null);

		assertNotNull(result);
		assertEquals("newuser", result.getUsername());
		assertNull(result.getParentId());
		assertNull(result.getEffectiveParentId());
	}

	@Test
	void testRegisterUser_DuplicateUsername() {
		when(userRepository.findByUsername("existing")).thenReturn(Optional.of(activeUser));

		assertThrows(IllegalArgumentException.class, () -> {
			userService.registerUser("existing", null);
		});
	}

	@Test
	void testMarkUserInactive_ShiftsChildren() {
		when(userRepository.findByIdWithLock(2L)).thenReturn(Optional.of(activeUser));
		when(userRepository.findByEffectiveParentId(2L)).thenReturn(Arrays.asList(childUser1, childUser2));
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
		when(referralShiftHistoryRepository.save(any(ReferralShiftHistory.class)))
			.thenAnswer(invocation -> invocation.getArgument(0));

		userService.markUserInactive(2L);

		// Verify user is marked inactive
		assertFalse(activeUser.getIsActive());
		assertNotNull(activeUser.getInactiveSince());

		// Verify children are shifted
		assertEquals(1L, childUser1.getEffectiveParentId());
		assertEquals(1L, childUser2.getEffectiveParentId());

		// Verify history entries created
		verify(referralShiftHistoryRepository, times(2)).save(any(ReferralShiftHistory.class));
		verify(userRepository, times(3)).save(any(User.class)); // activeUser + 2 children
	}

	@Test
	void testMarkUserInactive_AlreadyInactive() {
		activeUser.setIsActive(false);
		when(userRepository.findByIdWithLock(2L)).thenReturn(Optional.of(activeUser));

		userService.markUserInactive(2L);

		verify(userRepository, never()).findByEffectiveParentId(any());
		verify(referralShiftHistoryRepository, never()).save(any());
	}

	@Test
	void testReactivateUser_RevertsHistory() {
		activeUser.setIsActive(false);
		activeUser.setInactiveSince(OffsetDateTime.now().minusDays(1));

		ReferralShiftHistory history1 = new ReferralShiftHistory(3L, 2L, 2L, 1L);
		history1.setId(1L);
		history1.setReverted(false);

		ReferralShiftHistory history2 = new ReferralShiftHistory(4L, 2L, 2L, 1L);
		history2.setId(2L);
		history2.setReverted(false);

		when(userRepository.findByIdWithLock(2L)).thenReturn(Optional.of(activeUser));
		when(referralShiftHistoryRepository.findByInactiveUserIdAndRevertedFalse(2L))
			.thenReturn(Arrays.asList(history1, history2));
		when(userRepository.findByIdWithLock(3L)).thenReturn(Optional.of(childUser1));
		when(userRepository.findByIdWithLock(4L)).thenReturn(Optional.of(childUser2));
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
		when(referralShiftHistoryRepository.save(any(ReferralShiftHistory.class)))
			.thenAnswer(invocation -> invocation.getArgument(0));

		userService.reactivateUser(2L);

		// Verify user is reactivated
		assertTrue(activeUser.getIsActive());
		assertNull(activeUser.getInactiveSince());

		// Verify children are reverted
		assertEquals(2L, childUser1.getEffectiveParentId());
		assertEquals(2L, childUser2.getEffectiveParentId());

		// Verify history entries are marked as reverted
		assertTrue(history1.getReverted());
		assertTrue(history2.getReverted());
		assertNotNull(history1.getRevertAt());
		assertNotNull(history2.getRevertAt());

		verify(referralShiftHistoryRepository, times(2)).save(any(ReferralShiftHistory.class));
	}

	@Test
	void testUpdateLastSaleAt_ReactivatesInactiveUser() {
		activeUser.setIsActive(false);
		activeUser.setInactiveSince(OffsetDateTime.now().minusDays(1));

		when(userRepository.findByIdWithLock(2L)).thenReturn(Optional.of(activeUser));
		when(referralShiftHistoryRepository.findByInactiveUserIdAndRevertedFalse(2L))
			.thenReturn(new ArrayList<>());
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

		userService.updateLastSaleAt(2L);

		// Verify last sale timestamp is updated
		assertNotNull(activeUser.getLastSaleAt());

		// Verify user is reactivated
		assertTrue(activeUser.getIsActive());
		assertNull(activeUser.getInactiveSince());
	}
}

