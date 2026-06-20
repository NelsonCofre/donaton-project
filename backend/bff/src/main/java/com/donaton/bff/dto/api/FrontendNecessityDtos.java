package com.donaton.bff.dto.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public final class FrontendNecessityDtos {

	private FrontendNecessityDtos() {
	}

	@Schema(description = "Necesidad en formato consumido por el frontend")
	public record NecesidadResponse(
		long idNecesidad,
		String titulo,
		String descripcion,
		String recurso,
		int cantidad,
		String prioridad,
		String estado,
		String ubicacion,
		String fechaReporte
	) {
	}

	@Schema(description = "Datos para crear o actualizar una necesidad desde el frontend")
	public record CreateNecesidadRequest(
		@NotBlank String titulo,
		String descripcion,
		@NotBlank String recurso,
		@NotNull @Min(1) Integer cantidad,
		String prioridad,
		String estado,
		@NotBlank String ubicacion,
		@NotBlank String fechaReporte
	) {
	}
}
