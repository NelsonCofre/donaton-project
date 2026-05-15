package com.donaton.bff.dto.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class FrontendAuthDtos {

	private FrontendAuthDtos() {
	}

	public record LoginRequest(
		@NotBlank @Email String email,
		@NotBlank String password
	) {
	}

	public record RegisterRequest(
		@NotBlank @Email String email,
		@NotBlank @Size(min = 8, max = 100) String password,
		String rol
	) {
	}

	public record AuthResponse(String token, UserResponse user) {
	}

	public record UserResponse(long idUsuario, String email, String rol) {
	}
}
