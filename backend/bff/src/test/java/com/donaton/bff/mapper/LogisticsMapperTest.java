package com.donaton.bff.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.donaton.bff.dto.api.FrontendLogisticsDtos.CreateCollectionCenterRequest;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.CreateInventoryItemRequest;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.CreateShipmentRequest;
import com.donaton.bff.dto.logistics.LogisticsServiceDtos.CentroAcopioResponseDto;
import com.donaton.bff.dto.logistics.LogisticsServiceDtos.EnvioResponseDto;
import com.donaton.bff.dto.logistics.LogisticsServiceDtos.InventarioResponseDto;

class LogisticsMapperTest {

	@Test
	void toServiceRequestTrimsCollectionCenterFields() {
		var request = new CreateCollectionCenterRequest("  Centro Norte  ", "  Santiago  ");

		var serviceRequest = LogisticsMapper.toServiceRequest(request);

		assertThat(serviceRequest.name()).isEqualTo("Centro Norte");
		assertThat(serviceRequest.location()).isEqualTo("Santiago");
	}

	@Test
	void toFrontendMapsCollectionCenter() {
		var dto = new CentroAcopioResponseDto(1L, "Centro Sur", "Concepción");

		var response = LogisticsMapper.toFrontend(dto);

		assertThat(response.idCentro()).isEqualTo(1L);
		assertThat(response.nombre()).isEqualTo("Centro Sur");
		assertThat(response.ubicacion()).isEqualTo("Concepción");
	}

	@Test
	void toServiceRequestMapsInventory() {
		var request = new CreateInventoryItemRequest(4L, "  Agua  ", 50);

		var serviceRequest = LogisticsMapper.toServiceRequest(request);

		assertThat(serviceRequest.centerId()).isEqualTo(4L);
		assertThat(serviceRequest.resource()).isEqualTo("Agua");
		assertThat(serviceRequest.quantity()).isEqualTo(50);
	}

	@Test
	void toFrontendMapsInventoryWithCenterName() {
		var dto = new InventarioResponseDto(8L, 4L, "Frazadas", 20);

		var response = LogisticsMapper.toFrontend(dto, "Centro Norte");

		assertThat(response.idInventario()).isEqualTo(8L);
		assertThat(response.idCentro()).isEqualTo(4L);
		assertThat(response.centroNombre()).isEqualTo("Centro Norte");
		assertThat(response.recurso()).isEqualTo("Frazadas");
		assertThat(response.cantidad()).isEqualTo(20);
		assertThat(response.updatedAt()).isEqualTo(LocalDate.now().toString());
	}

	@Test
	void toServiceRequestMapsShipmentStatusAndDate() {
		var request = new CreateShipmentRequest(2L, "2026-06-20", "EN_TRANSITO");

		var serviceRequest = LogisticsMapper.toServiceRequest(request);

		assertThat(serviceRequest.centerId()).isEqualTo(2L);
		assertThat(serviceRequest.date()).isEqualTo(LocalDate.of(2026, 6, 20));
		assertThat(serviceRequest.status()).isEqualTo("IN_TRANSIT");
	}

	@Test
	void toServiceRequestDefaultsShipmentStatusToPlanned() {
		var request = new CreateShipmentRequest(2L, "2026-06-20", "DESCONOCIDO");

		var serviceRequest = LogisticsMapper.toServiceRequest(request);

		assertThat(serviceRequest.status()).isEqualTo("PLANNED");
	}

	@Test
	void toFrontendMapsShipmentStatus() {
		var dto = new EnvioResponseDto(9L, LocalDate.of(2026, 6, 21), "DELIVERED", 3L);

		var response = LogisticsMapper.toFrontend(dto, "Centro Envíos");

		assertThat(response.idEnvio()).isEqualTo(9L);
		assertThat(response.idCentro()).isEqualTo(3L);
		assertThat(response.centroNombre()).isEqualTo("Centro Envíos");
		assertThat(response.fecha()).isEqualTo("2026-06-21");
		assertThat(response.estado()).isEqualTo("ENTREGADO");
	}

	@Test
	void toFrontendDefaultsUnknownShipmentStatusToPreparacion() {
		var dto = new EnvioResponseDto(10L, LocalDate.of(2026, 6, 22), "UNKNOWN", 1L);

		var response = LogisticsMapper.toFrontend(dto, null);

		assertThat(response.estado()).isEqualTo("PREPARACION");
		assertThat(response.centroNombre()).isEmpty();
	}
}
