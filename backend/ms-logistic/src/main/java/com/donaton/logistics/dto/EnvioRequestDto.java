package com.donaton.logistics.dto;

import com.donaton.logistics.model.EstadoEnvio;
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
public class EnvioRequestDto {

	@NotNull
	private LocalDate date;

	@NotNull
	private EstadoEnvio status;

	@NotNull
	private Long centerId;
}
