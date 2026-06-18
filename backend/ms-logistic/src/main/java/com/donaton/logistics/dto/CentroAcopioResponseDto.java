package com.donaton.logistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Centro de acopio registrado en el sistema")
public class CentroAcopioResponseDto {

	@Schema(description = "Identificador único del centro de acopio", example = "1")
	private Long id;

	@Schema(description = "Nombre del centro de acopio", example = "Centro Norte")
	private String name;

	@Schema(description = "Ubicación física del centro", example = "Av. Principal 100, Santiago")
	private String location;
}
