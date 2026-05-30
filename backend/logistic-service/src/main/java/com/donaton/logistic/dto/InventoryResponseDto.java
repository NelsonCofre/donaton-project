package com.donaton.logistic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponseDto {

	private Long id;
	private Long centerId;
	private String resource;
	private Integer quantity;
}
