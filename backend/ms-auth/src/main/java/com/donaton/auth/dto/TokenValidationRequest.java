package com.donaton.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Solicitud de validación de access token JWT")
public record TokenValidationRequest(
	@Schema(description = "Access token JWT a validar", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
	@NotBlank
	String token
) {
}
