package com.donaton.logistics.dto;

import com.donaton.logistics.model.EstadoEnvio;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Datos para registrar o actualizar un envío")
public class EnvioRequestDto {

	@NotNull
	@Schema(description = "Fecha programada o de salida del envío", example = "2026-06-10")
	private LocalDate date;

	@NotNull
	@Schema(description = "Estado actual del envío", example = "PLANNED", allowableValues = {
		"PLANNED", "IN_TRANSIT", "DELIVERED", "CANCELLED"
	})
	private EstadoEnvio status;

	@NotNull
	@Schema(description = "Identificador del centro de acopio origen", example = "1")
	private Long centerId;
}
