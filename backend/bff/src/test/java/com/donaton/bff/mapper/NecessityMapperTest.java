package com.donaton.bff.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.donaton.bff.dto.api.FrontendNecessityDtos.CreateNecesidadRequest;
import com.donaton.bff.dto.necessity.NecessityServiceDtos.NecessityResponseDto;

class NecessityMapperTest {

	@Test
	void toServiceRequestTrimsFieldsAndParsesDate() {
		var request = new CreateNecesidadRequest(
			"  Municipalidad  ",
			"Descripción",
			" Frazadas ",
			30,
			"ALTA",
			"ABIERTA",
			" Valparaíso ",
			"2026-06-19T08:00:00"
		);

		var serviceRequest = NecessityMapper.toServiceRequest(request);

		assertThat(serviceRequest.resourceName()).isEqualTo("Frazadas");
		assertThat(serviceRequest.reportedBy()).isEqualTo("Municipalidad");
		assertThat(serviceRequest.location()).isEqualTo("Valparaíso");
		assertThat(serviceRequest.quantity()).isEqualTo(30);
		assertThat(serviceRequest.reportedDate()).isEqualTo(LocalDate.of(2026, 6, 19));
	}

	@Test
	void toServiceRequestUsesTodayWhenDateIsInvalid() {
		var request = new CreateNecesidadRequest(
			"Título",
			null,
			"Agua",
			5,
			null,
			null,
			"Santiago",
			"fecha-invalida"
		);

		var serviceRequest = NecessityMapper.toServiceRequest(request);

		assertThat(serviceRequest.reportedDate()).isEqualTo(LocalDate.now());
	}

	@Test
	void toFrontendUsesReportedByAsTitleWhenPresent() {
		var dto = new NecessityResponseDto(
			2L,
			"Agua",
			100,
			"Concepción",
			LocalDate.of(2026, 4, 10),
			"ONG Local"
		);

		var response = NecessityMapper.toFrontend(dto);

		assertThat(response.idNecesidad()).isEqualTo(2L);
		assertThat(response.titulo()).isEqualTo("ONG Local");
		assertThat(response.recurso()).isEqualTo("Agua");
		assertThat(response.cantidad()).isEqualTo(100);
		assertThat(response.ubicacion()).isEqualTo("Concepción");
		assertThat(response.prioridad()).isEqualTo("MEDIA");
		assertThat(response.estado()).isEqualTo("ABIERTA");
		assertThat(response.fechaReporte()).isEqualTo("2026-04-10");
	}

	@Test
	void toFrontendFallsBackToResourceWhenReportedByIsBlank() {
		var dto = new NecessityResponseDto(3L, "Medicamentos", 15, "Temuco", LocalDate.of(2026, 3, 1), "  ");

		var response = NecessityMapper.toFrontend(dto);

		assertThat(response.titulo()).isEqualTo("Medicamentos");
	}

	@Test
	void toFrontendHandlesNullFields() {
		var dto = new NecessityResponseDto(null, null, null, null, null, null);

		var response = NecessityMapper.toFrontend(dto);

		assertThat(response.idNecesidad()).isZero();
		assertThat(response.recurso()).isEmpty();
		assertThat(response.cantidad()).isZero();
		assertThat(response.ubicacion()).isEmpty();
		assertThat(response.fechaReporte()).isEmpty();
	}
}
