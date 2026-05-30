package com.donaton.logistic.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentResponseDto {

	private Long id;
	private LocalDate date;
	private String status;
	private Long centerId;
}
