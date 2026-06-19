package com.donaton.bff.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.donaton.bff.dto.api.FrontendNecessityDtos.CreateNecesidadRequest;
import com.donaton.bff.dto.api.FrontendNecessityDtos.NecesidadResponse;
import com.donaton.bff.dto.necessity.NecessityServiceDtos.NecessityRequestDto;
import com.donaton.bff.dto.necessity.NecessityServiceDtos.NecessityResponseDto;

public final class NecessityMapper {

	private static final String DEFAULT_PRIORIDAD = "MEDIA";
	private static final String DEFAULT_ESTADO = "ABIERTA";

	private NecessityMapper() {
	}

	public static NecessityRequestDto toServiceRequest(CreateNecesidadRequest request) {
		return new NecessityRequestDto(
			request.recurso().trim(),
			request.cantidad(),
			request.ubicacion().trim(),
			parseDate(request.fechaReporte()),
			request.titulo().trim()
		);
	}

	public static NecesidadResponse toFrontend(NecessityResponseDto dto) {
		String resource = dto.resourceName() != null ? dto.resourceName() : "";
		String reportedBy = dto.reportedBy() != null ? dto.reportedBy() : "";
		String location = dto.location() != null ? dto.location() : "";
		return new NecesidadResponse(
			dto.id() != null ? dto.id() : 0L,
			!reportedBy.isBlank() ? reportedBy : resource,
			"",
			resource,
			dto.quantity() != null ? dto.quantity() : 0,
			DEFAULT_PRIORIDAD,
			DEFAULT_ESTADO,
			location,
			dto.reportedDate() != null ? dto.reportedDate().toString() : ""
		);
	}

	private static LocalDate parseDate(String value) {
		if (value == null || value.isBlank()) {
			return LocalDate.now();
		}
		String normalized = value.trim();
		if (normalized.length() >= 10) {
			normalized = normalized.substring(0, 10);
		}
		try {
			return LocalDate.parse(normalized);
		} catch (DateTimeParseException ex) {
			return LocalDate.now();
		}
	}
}
