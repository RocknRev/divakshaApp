package com.rak.divaksha.repository;

import com.rak.divaksha.entity.CommissionLedger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommissionLedgerRepository extends JpaRepository<CommissionLedger, Long> {

	List<CommissionLedger> findBySaleId(Long saleId);

	Page<CommissionLedger> findByBeneficiaryUserId(Long beneficiaryUserId, Pageable pageable);

	Page<CommissionLedger> findBySellerUserId(Long sellerUserId, Pageable pageable);

}

