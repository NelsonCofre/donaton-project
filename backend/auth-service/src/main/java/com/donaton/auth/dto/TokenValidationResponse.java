package com.donaton.auth.dto;

import java.util.Set;

import com.donaton.auth.model.Role;

public record TokenValidationResponse(
	boolean valid,
	String subject,
	Role role,
	Set<String> permissions
) {
}
