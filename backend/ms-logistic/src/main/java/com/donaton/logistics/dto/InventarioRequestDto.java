package com.donaton.logistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para registrar o actualizar inventario en un centro de acopio")
public class InventarioRequestDto {

	@NotNull
	@Schema(description = "Identificador del centro de acopio asociado", example = "1")
	private Long centerId;

	@NotBlank
	@Schema(description = "Nombre del recurso almacenado", example = "Frazadas")
	private String resource;

	@NotNull
	@Min(1)
	@Schema(description = "Cantidad disponible del recurso", example = "200", minimum = "1")
	private Integer quantity;
}
