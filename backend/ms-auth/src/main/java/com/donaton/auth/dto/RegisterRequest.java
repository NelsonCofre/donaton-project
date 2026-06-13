package com.donaton.auth.dto;

import com.donaton.auth.model.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para registrar un nuevo usuario")
public record RegisterRequest(
	@Schema(description = "Correo electrónico del usuario", example = "usuario@donaton.cl")
	@NotBlank
	@Email
	String email,

	@Schema(description = "Contraseña (mínimo 8 caracteres)", example = "MiClaveSegura1", minLength = 8, maxLength = 100)
	@NotBlank
	@Size(min = 8, max = 100)
	String password,

	@Schema(description = "Rol asignado al usuario", example = "USER", allowableValues = { "ADMIN", "USER" })
	@NotNull
	Role role
) {
}
