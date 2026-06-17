package com.donaton.bff.dto.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class FrontendAuthDtos {

	private FrontendAuthDtos() {
	}

	@Schema(description = "Credenciales de inicio de sesión para el frontend")
	public record LoginRequest(
		@Schema(description = "Correo electrónico", example = "usuario@donaton.cl")
		@NotBlank @Email String email,

		@Schema(description = "Contraseña", example = "MiClaveSegura1")
		@NotBlank String password
	) {
	}

	@Schema(description = "Datos para registrar un usuario desde el frontend")
	public record RegisterRequest(
		@Schema(description = "Correo electrónico", example = "usuario@donaton.cl")
		@NotBlank @Email String email,

		@Schema(description = "Contraseña (mínimo 8 caracteres)", example = "MiClaveSegura1", minLength = 8, maxLength = 100)
		@NotBlank @Size(min = 8, max = 100) String password,

		@Schema(description = "Rol solicitado", example = "USER")
		String rol
	) {
	}

	@Schema(description = "Respuesta de login con token JWT para la UI")
	public record AuthResponse(
		@Schema(description = "Access token JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
		String token,

		@Schema(description = "Datos del usuario autenticado")
		UserResponse user
	) {
	}

	@Schema(description = "Datos del usuario en formato frontend")
	public record UserResponse(
		@Schema(description = "Identificador del usuario", example = "1")
		long idUsuario,

		@Schema(description = "Correo electrónico", example = "usuario@donaton.cl")
		String email,

		@Schema(description = "Rol del usuario", example = "USER")
		String rol
	) {
	}
}
