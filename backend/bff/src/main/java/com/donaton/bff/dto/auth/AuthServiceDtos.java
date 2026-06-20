package com.donaton.bff.dto.auth;

import java.util.Set;

public final class AuthServiceDtos {

	private AuthServiceDtos() {
	}

	public record LoginRequest(String email, String password) {
	}

	public record RegisterRequest(String email, String password, String role) {
	}

	public record AuthResponse(
		String accessToken,
		String refreshToken,
		String tokenType,
		long expiresInSeconds,
		UserResponse user
	) {
	}

	public record UserResponse(Long id, String email, String role, Set<String> permissions) {
	}

	public record TokenValidationRequest(String token) {
	}

	public record TokenValidationResponse(boolean valid, String subject, String role, Set<String> permissions) {
	}
}
