package com.rak.divaksha.repository;

import com.rak.divaksha.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

	Optional<User> findByReferralCode(String referralCode);

	Optional<User> findByAffiliateCode(String affiliateCode);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT u FROM User u WHERE u.id = :id")
	Optional<User> findByIdWithLock(@Param("id") Long id);

	List<User> findByEffectiveParentId(Long effectiveParentId);

	@Query("SELECT u FROM User u WHERE u.isActive = true AND u.lastSaleAt < :cutoffDate")
	List<User> findInactiveUsersSince(@Param("cutoffDate") OffsetDateTime cutoffDate);

	@Query("SELECT u FROM User u WHERE u.isActive = false")
	List<User> findAllInactiveUsers();
}

