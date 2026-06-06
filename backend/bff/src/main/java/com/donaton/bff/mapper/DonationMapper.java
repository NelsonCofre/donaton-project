package com.donaton.bff.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import com.donaton.bff.dto.api.FrontendDonationDtos.CreateDonacionRequest;
import com.donaton.bff.dto.api.FrontendDonationDtos.DonacionResponse;
import com.donaton.bff.dto.api.FrontendDonationDtos.DonanteSummary;
import com.donaton.bff.dto.donation.DonationServiceDtos.DonationRequestDto;
import com.donaton.bff.dto.donation.DonationServiceDtos.DonationResponseDto;

public final class DonationMapper {

	private static final String DEFAULT_ESTADO = "PENDIENTE";

	private DonationMapper() {
	}

	public static DonationRequestDto toServiceRequest(CreateDonacionRequest request) {
		return new DonationRequestDto(
			request.tipoRecurso().trim(),
			request.cantidad(),
			request.contactoDonante().trim(),
			parseDate(request.fecha()),
			request.nombreDonante().trim()
		);
	}

	public static DonacionResponse toFrontend(DonationResponseDto dto) {
		String fecha = dto.donationDate() != null ? dto.donationDate().toString() : "";
		return new DonacionResponse(
			dto.id() != null ? dto.id() : 0L,
			fecha,
			dto.quantity() != null ? dto.quantity() : 0,
			DEFAULT_ESTADO,
			0L,
			new DonanteSummary(
				dto.warehouseName() != null ? dto.warehouseName() : "",
				dto.origin() != null ? dto.origin() : ""
			),
			dto.resourceName() != null ? List.of(dto.resourceName()) : List.of()
		);
	}

	private static LocalDate parseDate(String fecha) {
		if (fecha == null || fecha.isBlank()) {
			return LocalDate.now();
		}
		String value = fecha.trim();
		if (value.length() >= 10) {
			value = value.substring(0, 10);
		}
		try {
			return LocalDate.parse(value);
		} catch (DateTimeParseException ex) {
			return LocalDate.now();
		}
	}
}
