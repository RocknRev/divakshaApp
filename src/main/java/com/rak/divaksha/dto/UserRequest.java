package com.rak.divaksha.dto;

import jakarta.validation.constraints.NotBlank;

public class UserRequest {

	@NotBlank(message = "Username is required")
	private String username;

	private Long parentId;

	// Constructors
	public UserRequest() {
	}

	public UserRequest(String username, Long parentId) {
		this.username = username;
		this.parentId = parentId;
	}

	// Getters and Setters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
}

