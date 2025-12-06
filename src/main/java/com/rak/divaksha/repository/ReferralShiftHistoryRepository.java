package com.rak.divaksha.repository;

import com.rak.divaksha.entity.ReferralShiftHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferralShiftHistoryRepository extends JpaRepository<ReferralShiftHistory, Long> {

	List<ReferralShiftHistory> findByInactiveUserId(Long inactiveUserId);

	List<ReferralShiftHistory> findByAffectedChildId(Long affectedChildId);

	List<ReferralShiftHistory> findByInactiveUserIdAndRevertedFalse(Long inactiveUserId);
}

