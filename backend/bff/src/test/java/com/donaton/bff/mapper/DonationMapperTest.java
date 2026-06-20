package com.donaton.bff.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.donaton.bff.dto.api.FrontendDonationDtos.CreateDonacionRequest;
import com.donaton.bff.dto.donation.DonationServiceDtos.DonationResponseDto;

class DonationMapperTest {

	@Test
	void toServiceRequestTrimsFieldsAndParsesDate() {
		var request = new CreateDonacionRequest(
			"  Ana Pérez  ",
			" ana@mail.com ",
			" Agua ",
			25,
			"2026-06-19T10:00:00"
		);

		var serviceRequest = DonationMapper.toServiceRequest(request);

		assertThat(serviceRequest.resourceName()).isEqualTo("Agua");
		assertThat(serviceRequest.origin()).isEqualTo("ana@mail.com");
		assertThat(serviceRequest.warehouseName()).isEqualTo("Ana Pérez");
		assertThat(serviceRequest.quantity()).isEqualTo(25);
		assertThat(serviceRequest.donationDate()).isEqualTo(LocalDate.of(2026, 6, 19));
	}

	@Test
	void toServiceRequestUsesTodayWhenDateIsBlank() {
		var request = new CreateDonacionRequest("Donante", "contacto@test.com", "Frazadas", 10, "   ");

		var serviceRequest = DonationMapper.toServiceRequest(request);

		assertThat(serviceRequest.donationDate()).isEqualTo(LocalDate.now());
	}

	@Test
	void toServiceRequestUsesTodayWhenDateIsInvalid() {
		var request = new CreateDonacionRequest("Donante", "contacto@test.com", "Frazadas", 10, "no-es-fecha");

		var serviceRequest = DonationMapper.toServiceRequest(request);

		assertThat(serviceRequest.donationDate()).isEqualTo(LocalDate.now());
	}

	@Test
	void toFrontendMapsDonationWithDefaults() {
		var dto = new DonationResponseDto(
			5L,
			"Medicamentos",
			40,
			"empresa@test.com",
			LocalDate.of(2026, 5, 1),
			"Empresa Salud"
		);

		var response = DonationMapper.toFrontend(dto);

		assertThat(response.idDonacion()).isEqualTo(5L);
		assertThat(response.fecha()).isEqualTo("2026-05-01");
		assertThat(response.cantidad()).isEqualTo(40);
		assertThat(response.estado()).isEqualTo("PENDIENTE");
		assertThat(response.donante().nombre()).isEqualTo("Empresa Salud");
		assertThat(response.donante().contacto()).isEqualTo("empresa@test.com");
		assertThat(response.recursoTipos()).isEqualTo(List.of("Medicamentos"));
	}

	@Test
	void toFrontendHandlesNullFields() {
		var dto = new DonationResponseDto(null, null, null, null, null, null);

		var response = DonationMapper.toFrontend(dto);

		assertThat(response.idDonacion()).isZero();
		assertThat(response.fecha()).isEmpty();
		assertThat(response.cantidad()).isZero();
		assertThat(response.donante().nombre()).isEmpty();
		assertThat(response.recursoTipos()).isEmpty();
	}
}
