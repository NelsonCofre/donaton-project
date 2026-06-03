package com.donaton.auth.dto;

import java.util.Set;

import com.donaton.auth.model.Role;

public record UserResponse(
	Long id,
	String email,
	Role role,
	Set<String> permissions
) {
}
