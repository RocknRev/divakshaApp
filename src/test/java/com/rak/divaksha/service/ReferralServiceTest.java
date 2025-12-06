package com.rak.divaksha.service;

import com.rak.divaksha.entity.CommissionLedger;
import com.rak.divaksha.entity.Sale;
import com.rak.divaksha.entity.User;
import com.rak.divaksha.repository.CommissionLedgerRepository;
import com.rak.divaksha.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReferralServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private CommissionLedgerRepository commissionLedgerRepository;

	@InjectMocks
	private ReferralService referralService;

	private Sale testSale;
	private User seller;
	private User parent1;
	private User parent2;
	private User parent3;

	@BeforeEach
	void setUp() {
		testSale = new Sale();
		testSale.setId(1L);
		testSale.setSellerUserId(1L);
		testSale.setTotalAmount(new BigDecimal("1000.00"));

		seller = new User();
		seller.setId(1L);
		seller.setIsActive(true);
		seller.setEffectiveParentId(2L);

		parent1 = new User();
		parent1.setId(2L);
		parent1.setIsActive(true);
		parent1.setEffectiveParentId(3L);

		parent2 = new User();
		parent2.setId(3L);
		parent2.setIsActive(true);
		parent2.setEffectiveParentId(4L);

		parent3 = new User();
		parent3.setId(4L);
		parent3.setIsActive(true);
		parent3.setEffectiveParentId(null);
	}

	@Test
	void testCalculateCommissions_SingleLevel() {
		when(userRepository.findByIdWithLock(1L)).thenReturn(Optional.of(seller));
		when(userRepository.findByIdWithLock(2L)).thenReturn(Optional.of(parent1));
		when(commissionLedgerRepository.save(any(CommissionLedger.class))).thenAnswer(invocation -> invocation.getArgument(0));

		List<CommissionLedger> result = referralService.calculateAndDistributeCommissions(testSale);

		assertEquals(2, result.size());

		// Level 0 (seller): 5%
		CommissionLedger sellerCommission = result.get(0);
		assertEquals(1L, sellerCommission.getBeneficiaryUserId());
		assertEquals(0, sellerCommission.getLevel());
		assertEquals(new BigDecimal("5.00"), sellerCommission.getPercentage());
		assertEquals(new BigDecimal("50.00"), sellerCommission.getAmount());

		// Level 1: 10%
		CommissionLedger parent1Commission = result.get(1);
		assertEquals(2L, parent1Commission.getBeneficiaryUserId());
		assertEquals(1, parent1Commission.getLevel());
		assertEquals(new BigDecimal("10.00"), parent1Commission.getPercentage());
		assertEquals(new BigDecimal("100.00"), parent1Commission.getAmount());

		verify(commissionLedgerRepository, times(2)).save(any(CommissionLedger.class));
	}

	@Test
	void testCalculateCommissions_MultipleLevels() {
		when(userRepository.findByIdWithLock(1L)).thenReturn(Optional.of(seller));
		when(userRepository.findByIdWithLock(2L)).thenReturn(Optional.of(parent1));
		when(userRepository.findByIdWithLock(3L)).thenReturn(Optional.of(parent2));
		when(userRepository.findByIdWithLock(4L)).thenReturn(Optional.of(parent3));
		when(commissionLedgerRepository.save(any(CommissionLedger.class))).thenAnswer(invocation -> invocation.getArgument(0));

		List<CommissionLedger> result = referralService.calculateAndDistributeCommissions(testSale);

		assertEquals(4, result.size());

		// Verify level 0 (seller)
		assertEquals(0, result.get(0).getLevel());
		assertEquals(new BigDecimal("50.00"), result.get(0).getAmount());

		// Verify level 1
		assertEquals(1, result.get(1).getLevel());
		assertEquals(new BigDecimal("100.00"), result.get(1).getAmount());

		// Verify level 2
		assertEquals(2, result.get(2).getLevel());
		assertEquals(new BigDecimal("90.00"), result.get(2).getAmount());

		// Verify level 3
		assertEquals(3, result.get(3).getLevel());
		assertEquals(new BigDecimal("80.00"), result.get(3).getAmount());
	}

	@Test
	void testCalculateCommissions_SkipsInactiveUplines() {
		parent1.setIsActive(false);

		when(userRepository.findByIdWithLock(1L)).thenReturn(Optional.of(seller));
		when(userRepository.findByIdWithLock(2L)).thenReturn(Optional.of(parent1));
		when(commissionLedgerRepository.save(any(CommissionLedger.class))).thenAnswer(invocation -> invocation.getArgument(0));

		List<CommissionLedger> result = referralService.calculateAndDistributeCommissions(testSale);

		// Only seller commission should be created (inactive parent skipped)
		assertEquals(1, result.size());
		assertEquals(0, result.get(0).getLevel());
		assertEquals(1L, result.get(0).getBeneficiaryUserId());
	}

	@Test
	void testCalculateCommissions_StopsAtNullParent() {
		seller.setEffectiveParentId(null);

		when(userRepository.findByIdWithLock(1L)).thenReturn(Optional.of(seller));
		when(commissionLedgerRepository.save(any(CommissionLedger.class))).thenAnswer(invocation -> invocation.getArgument(0));

		List<CommissionLedger> result = referralService.calculateAndDistributeCommissions(testSale);

		// Only seller commission
		assertEquals(1, result.size());
		assertEquals(0, result.get(0).getLevel());
	}
}

