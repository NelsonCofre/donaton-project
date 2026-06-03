package com.donaton.logistics.dto;

import com.donaton.logistics.model.EstadoEnvio;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnvioResponseDto {

	private Long id;
	private LocalDate date;
	private EstadoEnvio status;
	private Long centerId;
}
