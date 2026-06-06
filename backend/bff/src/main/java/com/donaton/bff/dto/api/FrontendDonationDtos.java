package com.donaton.bff.dto.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public final class FrontendDonationDtos {

	private FrontendDonationDtos() {
	}

	public record DonacionResponse(
		long idDonacion,
		String fecha,
		int cantidad,
		String estado,
		long idDonante,
		DonanteSummary donante,
		java.util.List<String> recursoTipos
	) {
	}

	public record DonanteSummary(String nombre, String contacto) {
	}

	public record CreateDonacionRequest(
		@NotBlank String nombreDonante,
		@NotBlank String contactoDonante,
		@NotBlank String tipoRecurso,
		@NotNull @Min(1) Integer cantidad,
		@NotBlank String fecha
	) {
	}
}
