package com.donaton.bff.dto.necessity;

import java.time.LocalDate;

public final class NecessityServiceDtos {

	private NecessityServiceDtos() {
	}

	public record NecessityRequestDto(
		String resourceName,
		Integer quantity,
		String location,
		LocalDate reportedDate,
		String reportedBy
	) {
	}

	public record NecessityResponseDto(
		Long id,
		String resourceName,
		Integer quantity,
		String location,
		LocalDate reportedDate,
		String reportedBy
	) {
	}
}
