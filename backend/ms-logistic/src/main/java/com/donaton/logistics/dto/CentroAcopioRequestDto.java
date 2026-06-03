package com.donaton.logistics.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CentroAcopioRequestDto {

	@NotBlank
	private String name;

	@NotBlank
	private String location;
}
