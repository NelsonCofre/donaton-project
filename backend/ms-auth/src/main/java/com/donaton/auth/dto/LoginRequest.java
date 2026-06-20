package com.donaton.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciales de inicio de sesión")
public record LoginRequest(
	@Schema(description = "Correo electrónico registrado", example = "usuario@donaton.cl")
	@NotBlank
	@Email
	String email,

	@Schema(description = "Contraseña del usuario", example = "MiClaveSegura1")
	@NotBlank
	String password
) {
}
