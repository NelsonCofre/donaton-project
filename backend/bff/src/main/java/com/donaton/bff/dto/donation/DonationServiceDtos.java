package com.donaton.bff.dto.donation;

import java.time.LocalDate;

public final class DonationServiceDtos {

	private DonationServiceDtos() {
	}

	public record DonationRequestDto(
		String resourceName,
		Integer quantity,
		String origin,
		LocalDate donationDate,
		String warehouseName
	) {
	}

	public record DonationResponseDto(
		Long id,
		String resourceName,
		Integer quantity,
		String origin,
		LocalDate donationDate,
		String warehouseName
	) {
	}
}
