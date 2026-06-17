package com.donaton.logistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para registrar o actualizar un centro de acopio")
public class CentroAcopioRequestDto {

	@NotBlank
	@Schema(description = "Nombre del centro de acopio", example = "Centro Norte")
	private String name;

	@NotBlank
	@Schema(description = "Ubicación física del centro", example = "Av. Principal 100, Santiago")
	private String location;
}
