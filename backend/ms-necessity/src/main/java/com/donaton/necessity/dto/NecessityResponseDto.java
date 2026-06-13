package com.donaton.necessity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Necesidad en terreno registrada en el sistema")
public class NecessityResponseDto {

	@Schema(description = "Identificador único de la necesidad", example = "1")
	private Long id;

	@Schema(description = "Nombre del recurso requerido", example = "Agua potable (litros)")
	private String resourceName;

	@Schema(description = "Cantidad requerida del recurso", example = "5000")
	private Integer quantity;

	@Schema(description = "Ubicación geográfica donde se reporta la necesidad", example = "Viña del Mar, campamento provisional")
	private String location;

	@Schema(description = "Fecha en que se reportó la necesidad", example = "2026-06-02")
	private LocalDate reportedDate;

	@Schema(description = "Entidad o persona que reporta la necesidad", example = "Centro Investigador Regional Valparaíso")
	private String reportedBy;
}
