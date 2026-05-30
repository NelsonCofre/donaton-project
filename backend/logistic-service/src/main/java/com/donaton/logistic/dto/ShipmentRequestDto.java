package com.donaton.logistic.dto;

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
public class ShipmentRequestDto {

	@NotNull
	private LocalDate date;

	@NotBlank
	private String status;

	@NotNull
	private Long centerId;
}
