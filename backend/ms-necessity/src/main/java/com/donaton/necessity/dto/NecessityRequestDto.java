package com.donaton.necessity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para registrar o actualizar una necesidad en terreno")
public class NecessityRequestDto {

	@NotBlank
	@Schema(description = "Nombre del recurso requerido", example = "Frazadas")
	private String resourceName;

	@NotNull
	@Min(1)
	@Schema(description = "Cantidad requerida del recurso", example = "200", minimum = "1")
	private Integer quantity;

	@NotBlank
	@Schema(description = "Ubicación geográfica donde se reporta la necesidad", example = "Valparaíso, sector Playa Ancha")
	private String location;

	@NotNull
	@Schema(description = "Fecha en que se reportó la necesidad", example = "2026-06-01")
	private LocalDate reportedDate;

	@NotBlank
	@Schema(description = "Entidad o persona que reporta la necesidad", example = "Municipalidad de Valparaíso")
	private String reportedBy;
}
