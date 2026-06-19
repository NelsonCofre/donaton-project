package com.donaton.bff.dto.logistics;

import java.time.LocalDate;

public final class LogisticsServiceDtos {

	private LogisticsServiceDtos() {
	}

	public record CentroAcopioRequestDto(String name, String location) {
	}

	public record CentroAcopioResponseDto(Long id, String name, String location) {
	}

	public record InventarioRequestDto(Long centerId, String resource, Integer quantity) {
	}

	public record InventarioResponseDto(Long id, Long centerId, String resource, Integer quantity) {
	}

	public record EnvioRequestDto(LocalDate date, String status, Long centerId) {
	}

	public record EnvioResponseDto(Long id, LocalDate date, String status, Long centerId) {
	}
}
