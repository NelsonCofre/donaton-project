package com.donaton.bff.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.donaton.bff.dto.logistics.LogisticsServiceDtos.CentroAcopioRequestDto;
import com.donaton.bff.dto.logistics.LogisticsServiceDtos.CentroAcopioResponseDto;
import com.donaton.bff.dto.logistics.LogisticsServiceDtos.EnvioRequestDto;
import com.donaton.bff.dto.logistics.LogisticsServiceDtos.EnvioResponseDto;
import com.donaton.bff.dto.logistics.LogisticsServiceDtos.InventarioRequestDto;
import com.donaton.bff.dto.logistics.LogisticsServiceDtos.InventarioResponseDto;

@Component
public class LogisticsServiceClient {

	private final RestClient restClient;

	public LogisticsServiceClient(@Qualifier("logisticsRestClient") RestClient restClient) {
		this.restClient = restClient;
	}

	public List<CentroAcopioResponseDto> listCenters() {
		CentroAcopioResponseDto[] body = RestClientHelper.withErrorHandling(
			restClient.get().uri("/api/v1/logistics/collection-centers").retrieve()
		).body(CentroAcopioResponseDto[].class);
		return body == null ? List.of() : Arrays.asList(body);
	}

	public CentroAcopioResponseDto getCenterById(long id) {
		return RestClientHelper.withErrorHandling(
			restClient.get()
				.uri("/api/v1/logistics/collection-centers/{id}", id)
				.retrieve()
		).body(CentroAcopioResponseDto.class);
	}

	public CentroAcopioResponseDto createCenter(CentroAcopioRequestDto request) {
		return RestClientHelper.withErrorHandling(
			restClient.post()
				.uri("/api/v1/logistics/collection-centers")
				.body(request)
				.retrieve()
		).body(CentroAcopioResponseDto.class);
	}

	public CentroAcopioResponseDto updateCenter(long id, CentroAcopioRequestDto request) {
		return RestClientHelper.withErrorHandling(
			restClient.put()
				.uri("/api/v1/logistics/collection-centers/{id}", id)
				.body(request)
				.retrieve()
		).body(CentroAcopioResponseDto.class);
	}

	public void deleteCenter(long id) {
		RestClientHelper.withErrorHandling(
			restClient.delete()
				.uri("/api/v1/logistics/collection-centers/{id}", id)
				.retrieve()
		).toBodilessEntity();
	}

	public List<InventarioResponseDto> listInventory() {
		InventarioResponseDto[] body = RestClientHelper.withErrorHandling(
			restClient.get().uri("/api/v1/logistics/inventories").retrieve()
		).body(InventarioResponseDto[].class);
		return body == null ? List.of() : Arrays.asList(body);
	}

	public InventarioResponseDto createInventory(InventarioRequestDto request) {
		return RestClientHelper.withErrorHandling(
			restClient.post()
				.uri("/api/v1/logistics/inventories")
				.body(request)
				.retrieve()
		).body(InventarioResponseDto.class);
	}

	public InventarioResponseDto updateInventory(long id, InventarioRequestDto request) {
		return RestClientHelper.withErrorHandling(
			restClient.put()
				.uri("/api/v1/logistics/inventories/{id}", id)
				.body(request)
				.retrieve()
		).body(InventarioResponseDto.class);
	}

	public void deleteInventory(long id) {
		RestClientHelper.withErrorHandling(
			restClient.delete()
				.uri("/api/v1/logistics/inventories/{id}", id)
				.retrieve()
		).toBodilessEntity();
	}

	public List<EnvioResponseDto> listShipments() {
		EnvioResponseDto[] body = RestClientHelper.withErrorHandling(
			restClient.get().uri("/api/v1/logistics/shipments").retrieve()
		).body(EnvioResponseDto[].class);
		return body == null ? List.of() : Arrays.asList(body);
	}

	public EnvioResponseDto createShipment(EnvioRequestDto request) {
		return RestClientHelper.withErrorHandling(
			restClient.post()
				.uri("/api/v1/logistics/shipments")
				.body(request)
				.retrieve()
		).body(EnvioResponseDto.class);
	}

	public EnvioResponseDto updateShipment(long id, EnvioRequestDto request) {
		return RestClientHelper.withErrorHandling(
			restClient.put()
				.uri("/api/v1/logistics/shipments/{id}", id)
				.body(request)
				.retrieve()
		).body(EnvioResponseDto.class);
	}

	public void deleteShipment(long id) {
		RestClientHelper.withErrorHandling(
			restClient.delete()
				.uri("/api/v1/logistics/shipments/{id}", id)
				.retrieve()
		).toBodilessEntity();
	}
}
