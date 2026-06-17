package com.donaton.bff.dto.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public final class FrontendDonationDtos {

	private FrontendDonationDtos() {
	}

	@Schema(description = "Donación en formato consumido por el frontend")
	public record DonacionResponse(
		@Schema(description = "Identificador de la donación", example = "1")
		long idDonacion,

		@Schema(description = "Fecha de la donación", example = "2026-06-08")
		String fecha,

		@Schema(description = "Cantidad donada", example = "150")
		int cantidad,

		@Schema(description = "Estado de la donación", example = "REGISTRADA")
		String estado,

		@Schema(description = "Identificador del donante", example = "1")
		long idDonante,

		@Schema(description = "Resumen del donante")
		DonanteSummary donante,

		@Schema(description = "Tipos de recurso asociados a la donación", example = "[\"Alimentos no perecederos\"]")
		java.util.List<String> recursoTipos
	) {
	}

	@Schema(description = "Datos resumidos del donante")
	public record DonanteSummary(
		@Schema(description = "Nombre del donante", example = "Empresa Alimentos del Sur S.A.")
		String nombre,

		@Schema(description = "Contacto del donante", example = "contacto@alimentosdelsur.cl")
		String contacto
	) {
	}

	@Schema(description = "Datos para crear o actualizar una donación desde el frontend")
	public record CreateDonacionRequest(
		@Schema(description = "Nombre del donante", example = "María González")
		@NotBlank String nombreDonante,

		@Schema(description = "Contacto del donante", example = "maria.gonzalez@email.com")
		@NotBlank String contactoDonante,

		@Schema(description = "Tipo de recurso donado", example = "Kits de higiene personal")
		@NotBlank String tipoRecurso,

		@Schema(description = "Cantidad donada", example = "50", minimum = "1")
		@NotNull @Min(1) Integer cantidad,

		@Schema(description = "Fecha de la donación (formato acordado con la UI)", example = "2026-06-08")
		@NotBlank String fecha
	) {
	}
}
