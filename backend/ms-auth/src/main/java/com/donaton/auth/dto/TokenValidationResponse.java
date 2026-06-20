package com.donaton.auth.dto;

import java.util.Set;

import com.donaton.auth.model.Role;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resultado de la validación de un access token JWT")
public record TokenValidationResponse(
	@Schema(description = "Indica si el token es válido y no está revocado", example = "true")
	boolean valid,

	@Schema(description = "Subject del token (email del usuario)", example = "usuario@donaton.cl")
	String subject,

	@Schema(description = "Rol asociado al token", example = "USER")
	Role role,

	@Schema(description = "Permisos asociados al rol", example = "[\"USER_READ\"]")
	Set<String> permissions
) {
}
