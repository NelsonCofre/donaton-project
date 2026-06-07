package com.donaton.necessity.dto;

import jakarta.validation.constraints.Min;
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
public class NecessityRequestDto {

	@NotBlank
	private String resourceName;

	@NotNull
	@Min(1)
	private Integer quantity;

	@NotBlank
	private String location;

	@NotNull
	private LocalDate reportedDate;

	@NotBlank
	private String reportedBy;
}
