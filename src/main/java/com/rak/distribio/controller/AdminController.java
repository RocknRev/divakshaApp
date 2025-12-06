package com.rak.distribio.controller;

import com.rak.distribio.dto.PagedResponse;
import com.rak.distribio.dto.ReferralShiftHistoryResponse;
import com.rak.distribio.entity.CommissionLedger;
import com.rak.distribio.entity.ReferralShiftHistory;
import com.rak.distribio.repository.CommissionLedgerRepository;
import com.rak.distribio.repository.ReferralShiftHistoryRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	private final CommissionLedgerRepository commissionLedgerRepository;
	private final ReferralShiftHistoryRepository referralShiftHistoryRepository;

	public AdminController(CommissionLedgerRepository commissionLedgerRepository,
	                       ReferralShiftHistoryRepository referralShiftHistoryRepository) {
		this.commissionLedgerRepository = commissionLedgerRepository;
		this.referralShiftHistoryRepository = referralShiftHistoryRepository;
	}

	@GetMapping("/ledger")
	public ResponseEntity<PagedResponse<CommissionLedger>> getCommissionLedger(@RequestParam int page, @RequestParam int size, @RequestParam Long beneficiaryUserId) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
		Page<CommissionLedger> ledgerPage = commissionLedgerRepository.findByBeneficiaryUserId(beneficiaryUserId, pageable);
		return ResponseEntity.ok(new PagedResponse<>(ledgerPage));
	}

	@GetMapping("/shift-history")
	public ResponseEntity<List<ReferralShiftHistoryResponse>> getShiftHistory() {
		List<ReferralShiftHistory> historyEntries = referralShiftHistoryRepository.findAll();
		List<ReferralShiftHistoryResponse> responses = historyEntries.stream()
			.map(this::mapToResponse)
			.collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}

	private ReferralShiftHistoryResponse mapToResponse(ReferralShiftHistory history) {
		ReferralShiftHistoryResponse response = new ReferralShiftHistoryResponse();
		response.setId(history.getId());
		response.setAffectedChildId(history.getAffectedChildId());
		response.setInactiveUserId(history.getInactiveUserId());
		response.setPreviousEffectiveParentId(history.getPreviousEffectiveParentId());
		response.setNewEffectiveParentId(history.getNewEffectiveParentId());
		response.setChangedAt(history.getChangedAt());
		response.setReverted(history.getReverted());
		response.setRevertAt(history.getRevertAt());
		return response;
	}
}

