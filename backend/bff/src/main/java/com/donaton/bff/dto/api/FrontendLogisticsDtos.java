package com.donaton.bff.dto.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public final class FrontendLogisticsDtos {

	private FrontendLogisticsDtos() {
	}

	@Schema(description = "Centro de acopio en formato consumido por el frontend")
	public record CollectionCenterResponse(
		long idCentro,
		String nombre,
		String ubicacion,
		String responsable,
		String telefono,
		int capacidad
	) {
	}

	public record CreateCollectionCenterRequest(
		@NotBlank String nombre,
		@NotBlank String ubicacion,
		String responsable,
		String telefono,
		Integer capacidad
	) {
	}

	@Schema(description = "Inventario en formato consumido por el frontend")
	public record InventoryItemResponse(
		long idInventario,
		long idCentro,
		String centroNombre,
		String recurso,
		int cantidad,
		String updatedAt
	) {
	}

	public record CreateInventoryItemRequest(
		@NotNull Long idCentro,
		@NotBlank String recurso,
		@NotNull @Min(1) Integer cantidad
	) {
	}

	@Schema(description = "Envío en formato consumido por el frontend")
	public record ShipmentResponse(
		long idEnvio,
		long idCentro,
		String centroNombre,
		String destino,
		String detalle,
		int cantidad,
		String fecha,
		String estado
	) {
	}

	public record CreateShipmentRequest(
		@NotNull Long idCentro,
		String destino,
		String detalle,
		Integer cantidad,
		@NotBlank String fecha,
		@NotBlank String estado
	) {
	}
}
