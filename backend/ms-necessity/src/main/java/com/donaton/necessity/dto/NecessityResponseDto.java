package com.donaton.necessity.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NecessityResponseDto {

	private Long id;
	private String resourceName;
	private Integer quantity;
	private String location;
	private LocalDate reportedDate;
	private String reportedBy;
}
