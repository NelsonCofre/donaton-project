package com.donaton.bff.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.donaton.bff.client.LogisticsServiceClient;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.CreateInventoryItemRequest;
import com.donaton.bff.dto.logistics.LogisticsServiceDtos.CentroAcopioResponseDto;
import com.donaton.bff.dto.logistics.LogisticsServiceDtos.EnvioResponseDto;
import com.donaton.bff.dto.logistics.LogisticsServiceDtos.InventarioRequestDto;
import com.donaton.bff.dto.logistics.LogisticsServiceDtos.InventarioResponseDto;

@ExtendWith(MockitoExtension.class)
class LogisticsBffServiceTest {

	@Mock
	private LogisticsServiceClient logisticsServiceClient;

	@InjectMocks
	private LogisticsBffService logisticsBffService;

	@Test
	void listInventoryEnrichesItemsWithCenterName() {
		when(logisticsServiceClient.listCenters()).thenReturn(List.of(
			new CentroAcopioResponseDto(1L, "Centro Norte", "Santiago")
		));
		when(logisticsServiceClient.listInventory()).thenReturn(List.of(
			new InventarioResponseDto(10L, 1L, "Agua", 20)
		));

		var result = logisticsBffService.listInventory();

		assertThat(result).hasSize(1);
		assertThat(result.getFirst().idInventario()).isEqualTo(10L);
		assertThat(result.getFirst().idCentro()).isEqualTo(1L);
		assertThat(result.getFirst().centroNombre()).isEqualTo("Centro Norte");
		assertThat(result.getFirst().recurso()).isEqualTo("Agua");
		assertThat(result.getFirst().cantidad()).isEqualTo(20);
	}

	@Test
	void createInventoryEnrichesCreatedItemWithCenterName() {
		var request = new CreateInventoryItemRequest(2L, "Ropa", 5);
		when(logisticsServiceClient.createInventory(new InventarioRequestDto(2L, "Ropa", 5)))
			.thenReturn(new InventarioResponseDto(11L, 2L, "Ropa", 5));
		when(logisticsServiceClient.listCenters()).thenReturn(List.of(
			new CentroAcopioResponseDto(2L, "Centro Sur", "Concepción")
		));

		var result = logisticsBffService.createInventory(request);

		assertThat(result.idInventario()).isEqualTo(11L);
		assertThat(result.centroNombre()).isEqualTo("Centro Sur");
		verify(logisticsServiceClient).createInventory(new InventarioRequestDto(2L, "Ropa", 5));
	}

	@Test
	void listShipmentsEnrichesItemsWithCenterName() {
		when(logisticsServiceClient.listCenters()).thenReturn(List.of(
			new CentroAcopioResponseDto(3L, "Centro Este", "Valparaíso")
		));
		when(logisticsServiceClient.listShipments()).thenReturn(List.of(
			new EnvioResponseDto(7L, LocalDate.of(2026, 7, 1), "PLANNED", 3L)
		));

		var result = logisticsBffService.listShipments();

		assertThat(result).hasSize(1);
		assertThat(result.getFirst().idEnvio()).isEqualTo(7L);
		assertThat(result.getFirst().centroNombre()).isEqualTo("Centro Este");
		assertThat(result.getFirst().estado()).isEqualTo("PREPARACION");
	}
}
