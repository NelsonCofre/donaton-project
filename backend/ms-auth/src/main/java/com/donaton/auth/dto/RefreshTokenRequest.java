package com.donaton.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Solicitud para renovar la sesión con refresh token")
public record RefreshTokenRequest(
	@Schema(description = "Refresh token emitido en login o register", example = "8f3c2a1b-refresh-token-example")
	@NotBlank
	String refreshToken
) {
}
