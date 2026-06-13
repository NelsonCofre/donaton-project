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
@Schema(description = "Registro de inventario en un centro de acopio")
public class InventarioResponseDto {

	@Schema(description = "Identificador único del inventario", example = "1")
	private Long id;

	@Schema(description = "Identificador del centro de acopio asociado", example = "1")
	private Long centerId;

	@Schema(description = "Nombre del recurso almacenado", example = "Agua potable (litros)")
	private String resource;

	@Schema(description = "Cantidad disponible del recurso", example = "5000")
	private Integer quantity;
}
