package com.donaton.auth.dto;

import java.util.Set;

import com.donaton.auth.model.Role;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos públicos del usuario autenticado")
public record UserResponse(
	@Schema(description = "Identificador único del usuario", example = "1")
	Long id,

	@Schema(description = "Correo electrónico", example = "usuario@donaton.cl")
	String email,

	@Schema(description = "Rol del usuario", example = "USER")
	Role role,

	@Schema(description = "Permisos efectivos del rol", example = "[\"USER_READ\"]")
	Set<String> permissions
) {
}
