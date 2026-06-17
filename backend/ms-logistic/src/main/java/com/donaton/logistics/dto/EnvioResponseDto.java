package com.donaton.logistics.dto;

import com.donaton.logistics.model.EstadoEnvio;
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
@Schema(description = "Envío de recursos desde un centro de acopio")
public class EnvioResponseDto {

	@Schema(description = "Identificador único del envío", example = "1")
	private Long id;

	@Schema(description = "Fecha programada o de salida del envío", example = "2026-06-10")
	private LocalDate date;

	@Schema(description = "Estado actual del envío", example = "IN_TRANSIT")
	private EstadoEnvio status;

	@Schema(description = "Identificador del centro de acopio origen", example = "1")
	private Long centerId;
}
