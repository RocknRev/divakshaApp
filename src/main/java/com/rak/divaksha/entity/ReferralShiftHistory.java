package com.rak.divaksha.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "referral_shift_history")
public class ReferralShiftHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "affected_child_id")
	private Long affectedChildId;

	@Column(name = "inactive_user_id")
	private Long inactiveUserId;

	@Column(name = "previous_effective_parent_id")
	private Long previousEffectiveParentId;

	@Column(name = "new_effective_parent_id")
	private Long newEffectiveParentId;

	@CreationTimestamp
	@Column(name = "changed_at", nullable = false, updatable = false)
	private OffsetDateTime changedAt;

	@Column(name = "reverted", nullable = false)
	private Boolean reverted = false;

	@Column(name = "revert_at")
	private OffsetDateTime revertAt;

	// Constructors
	public ReferralShiftHistory() {
	}

	public ReferralShiftHistory(Long affectedChildId, Long inactiveUserId, Long previousEffectiveParentId, Long newEffectiveParentId) {
		this.affectedChildId = affectedChildId;
		this.inactiveUserId = inactiveUserId;
		this.previousEffectiveParentId = previousEffectiveParentId;
		this.newEffectiveParentId = newEffectiveParentId;
		this.reverted = false;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAffectedChildId() {
		return affectedChildId;
	}

	public void setAffectedChildId(Long affectedChildId) {
		this.affectedChildId = affectedChildId;
	}

	public Long getInactiveUserId() {
		return inactiveUserId;
	}

	public void setInactiveUserId(Long inactiveUserId) {
		this.inactiveUserId = inactiveUserId;
	}

	public Long getPreviousEffectiveParentId() {
		return previousEffectiveParentId;
	}

	public void setPreviousEffectiveParentId(Long previousEffectiveParentId) {
		this.previousEffectiveParentId = previousEffectiveParentId;
	}

	public Long getNewEffectiveParentId() {
		return newEffectiveParentId;
	}

	public void setNewEffectiveParentId(Long newEffectiveParentId) {
		this.newEffectiveParentId = newEffectiveParentId;
	}

	public OffsetDateTime getChangedAt() {
		return changedAt;
	}

	public void setChangedAt(OffsetDateTime changedAt) {
		this.changedAt = changedAt;
	}

	public Boolean getReverted() {
		return reverted;
	}

	public void setReverted(Boolean reverted) {
		this.reverted = reverted;
	}

	public OffsetDateTime getRevertAt() {
		return revertAt;
	}

	public void setRevertAt(OffsetDateTime revertAt) {
		this.revertAt = revertAt;
	}
}

