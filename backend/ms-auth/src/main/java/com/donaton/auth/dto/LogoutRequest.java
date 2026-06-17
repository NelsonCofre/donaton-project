package com.donaton.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos opcionales para cerrar sesión y revocar refresh token")
public record LogoutRequest(
	@Schema(description = "Refresh token a revocar (opcional)", example = "8f3c2a1b-refresh-token-example")
	String refreshToken
) {
}
