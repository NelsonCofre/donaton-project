package com.donaton.bff.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

import com.donaton.bff.dto.api.FrontendLogisticsDtos.CollectionCenterResponse;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.CreateCollectionCenterRequest;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.CreateInventoryItemRequest;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.CreateShipmentRequest;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.InventoryItemResponse;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.ShipmentResponse;
import com.donaton.bff.dto.logistics.LogisticsServiceDtos.CentroAcopioRequestDto;
import com.donaton.bff.dto.logistics.LogisticsServiceDtos.CentroAcopioResponseDto;
import com.donaton.bff.dto.logistics.LogisticsServiceDtos.EnvioRequestDto;
import com.donaton.bff.dto.logistics.LogisticsServiceDtos.EnvioResponseDto;
import com.donaton.bff.dto.logistics.LogisticsServiceDtos.InventarioRequestDto;
import com.donaton.bff.dto.logistics.LogisticsServiceDtos.InventarioResponseDto;

public final class LogisticsMapper {

	private static final Map<String, String> FRONT_TO_SERVICE_STATUS = Map.of(
		"PREPARACION", "PLANNED",
		"EN_TRANSITO", "IN_TRANSIT",
		"ENTREGADO", "DELIVERED",
		"CANCELADO", "CANCELLED"
	);
	private static final Map<String, String> SERVICE_TO_FRONT_STATUS = Map.of(
		"PLANNED", "PREPARACION",
		"IN_TRANSIT", "EN_TRANSITO",
		"DELIVERED", "ENTREGADO",
		"CANCELLED", "CANCELADO"
	);

	private LogisticsMapper() {
	}

	public static CentroAcopioRequestDto toServiceRequest(CreateCollectionCenterRequest request) {
		return new CentroAcopioRequestDto(
			request.nombre().trim(),
			request.ubicacion().trim()
		);
	}

	public static CollectionCenterResponse toFrontend(CentroAcopioResponseDto dto) {
		return new CollectionCenterResponse(
			dto.id() != null ? dto.id() : 0L,
			dto.name() != null ? dto.name() : "",
			dto.location() != null ? dto.location() : ""
		);
	}

	public static InventarioRequestDto toServiceRequest(CreateInventoryItemRequest request) {
		return new InventarioRequestDto(
			request.idCentro(),
			request.recurso().trim(),
			request.cantidad()
		);
	}

	public static InventoryItemResponse toFrontend(
		InventarioResponseDto dto,
		String centerName
	) {
		return new InventoryItemResponse(
			dto.id() != null ? dto.id() : 0L,
			dto.centerId() != null ? dto.centerId() : 0L,
			centerName != null ? centerName : "",
			dto.resource() != null ? dto.resource() : "",
			dto.quantity() != null ? dto.quantity() : 0,
			LocalDate.now().toString()
		);
	}

	public static EnvioRequestDto toServiceRequest(CreateShipmentRequest request) {
		return new EnvioRequestDto(
			parseDate(request.fecha()),
			toServiceStatus(request.estado()),
			request.idCentro()
		);
	}

	public static ShipmentResponse toFrontend(EnvioResponseDto dto, String centerName) {
		return new ShipmentResponse(
			dto.id() != null ? dto.id() : 0L,
			dto.centerId() != null ? dto.centerId() : 0L,
			centerName != null ? centerName : "",
			dto.date() != null ? dto.date().toString() : "",
			toFrontendStatus(dto.status())
		);
	}

	private static String toServiceStatus(String status) {
		if (status == null || status.isBlank()) {
			return "PLANNED";
		}
		return FRONT_TO_SERVICE_STATUS.getOrDefault(status.trim().toUpperCase(), "PLANNED");
	}

	private static String toFrontendStatus(String status) {
		if (status == null || status.isBlank()) {
			return "PREPARACION";
		}
		return SERVICE_TO_FRONT_STATUS.getOrDefault(status.trim().toUpperCase(), "PREPARACION");
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
