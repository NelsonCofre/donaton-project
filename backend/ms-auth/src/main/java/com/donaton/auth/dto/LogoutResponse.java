package com.donaton.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Confirmación de cierre de sesión")
public record LogoutResponse(
	@Schema(description = "Mensaje de resultado", example = "Logout successful")
	String message
) {
}
