package com.donaton.logistics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventarioResponseDto {

	private Long id;
	private Long centerId;
	private String resource;
	private Integer quantity;
}
