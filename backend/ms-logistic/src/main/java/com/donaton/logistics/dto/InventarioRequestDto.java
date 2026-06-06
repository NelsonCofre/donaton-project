package com.donaton.logistics.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventarioRequestDto {

	@NotNull
	private Long centerId;

	@NotBlank
	private String resource;

	@NotNull
	@Min(1)
	private Integer quantity;
}
