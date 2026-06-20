package com.donaton.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de autenticación con tokens JWT")
public record AuthResponse(
	@Schema(description = "Access token JWT para endpoints protegidos", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
	String accessToken,

	@Schema(description = "Refresh token para renovar la sesión", example = "8f3c2a1b-refresh-token-example")
	String refreshToken,

	@Schema(description = "Tipo de token", example = "Bearer")
	String tokenType,

	@Schema(description = "Segundos hasta expiración del access token", example = "900")
	long expiresInSeconds,

	@Schema(description = "Datos del usuario autenticado")
	UserResponse user
) {
}
