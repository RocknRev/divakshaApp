package com.rak.distribio.dto;

import java.time.OffsetDateTime;

public class ReferralShiftHistoryResponse {

	private Long id;
	private Long affectedChildId;
	private Long inactiveUserId;
	private Long previousEffectiveParentId;
	private Long newEffectiveParentId;
	private OffsetDateTime changedAt;
	private Boolean reverted;
	private OffsetDateTime revertAt;

	// Constructors
	public ReferralShiftHistoryResponse() {
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

