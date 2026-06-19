package com.donaton.bff.controller;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.donaton.bff.client.LogisticsServiceClient;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.CollectionCenterResponse;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.CreateCollectionCenterRequest;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.CreateInventoryItemRequest;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.CreateShipmentRequest;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.InventoryItemResponse;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.ShipmentResponse;
import com.donaton.bff.dto.logistics.LogisticsServiceDtos.CentroAcopioResponseDto;
import com.donaton.bff.mapper.LogisticsMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Logística (Frontend)", description = "Centros, inventario y envíos adaptados para el frontend")
@Validated
@RestController
@RequestMapping("/api/v1/logistics")
public class LogisticsBffController {

	private final LogisticsServiceClient logisticsServiceClient;

	public LogisticsBffController(LogisticsServiceClient logisticsServiceClient) {
		this.logisticsServiceClient = logisticsServiceClient;
	}

	@GetMapping("/collection-centers")
	public java.util.List<CollectionCenterResponse> listCenters() {
		return logisticsServiceClient.listCenters().stream()
			.map(LogisticsMapper::toFrontend)
			.toList();
	}

	@GetMapping("/collection-centers/{id}")
	public CollectionCenterResponse getCenterById(@PathVariable long id) {
		return LogisticsMapper.toFrontend(logisticsServiceClient.getCenterById(id));
	}

	@PostMapping("/collection-centers")
	public ResponseEntity<CollectionCenterResponse> createCenter(
		@Valid @RequestBody CreateCollectionCenterRequest request
	) {
		var created = logisticsServiceClient.createCenter(LogisticsMapper.toServiceRequest(request));
		return ResponseEntity.status(HttpStatus.CREATED).body(LogisticsMapper.toFrontend(created));
	}

	@PutMapping("/collection-centers/{id}")
	public CollectionCenterResponse updateCenter(
		@PathVariable long id,
		@Valid @RequestBody CreateCollectionCenterRequest request
	) {
		var updated = logisticsServiceClient.updateCenter(id, LogisticsMapper.toServiceRequest(request));
		return LogisticsMapper.toFrontend(updated);
	}

	@DeleteMapping("/collection-centers/{id}")
	public ResponseEntity<Void> deleteCenter(@PathVariable long id) {
		logisticsServiceClient.deleteCenter(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/inventories")
	public java.util.List<InventoryItemResponse> listInventory() {
		Map<Long, String> centerNames = centerNames();
		return logisticsServiceClient.listInventory().stream()
			.map(item -> LogisticsMapper.toFrontend(item, centerNames.get(item.centerId())))
			.toList();
	}

	@PostMapping("/inventories")
	public ResponseEntity<InventoryItemResponse> createInventory(
		@Valid @RequestBody CreateInventoryItemRequest request
	) {
		var created = logisticsServiceClient.createInventory(LogisticsMapper.toServiceRequest(request));
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(LogisticsMapper.toFrontend(created, centerNames().get(created.centerId())));
	}

	@PutMapping("/inventories/{id}")
	public InventoryItemResponse updateInventory(
		@PathVariable long id,
		@Valid @RequestBody CreateInventoryItemRequest request
	) {
		var updated = logisticsServiceClient.updateInventory(id, LogisticsMapper.toServiceRequest(request));
		return LogisticsMapper.toFrontend(updated, centerNames().get(updated.centerId()));
	}

	@DeleteMapping("/inventories/{id}")
	public ResponseEntity<Void> deleteInventory(@PathVariable long id) {
		logisticsServiceClient.deleteInventory(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/shipments")
	public java.util.List<ShipmentResponse> listShipments() {
		Map<Long, String> centerNames = centerNames();
		return logisticsServiceClient.listShipments().stream()
			.map(item -> LogisticsMapper.toFrontend(item, centerNames.get(item.centerId())))
			.toList();
	}

	@PostMapping("/shipments")
	public ResponseEntity<ShipmentResponse> createShipment(
		@Valid @RequestBody CreateShipmentRequest request
	) {
		var created = logisticsServiceClient.createShipment(LogisticsMapper.toServiceRequest(request));
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(LogisticsMapper.toFrontend(created, centerNames().get(created.centerId())));
	}

	@PutMapping("/shipments/{id}")
	public ShipmentResponse updateShipment(
		@PathVariable long id,
		@Valid @RequestBody CreateShipmentRequest request
	) {
		var updated = logisticsServiceClient.updateShipment(id, LogisticsMapper.toServiceRequest(request));
		return LogisticsMapper.toFrontend(updated, centerNames().get(updated.centerId()));
	}

	@DeleteMapping("/shipments/{id}")
	public ResponseEntity<Void> deleteShipment(@PathVariable long id) {
		logisticsServiceClient.deleteShipment(id);
		return ResponseEntity.noContent().build();
	}

	private Map<Long, String> centerNames() {
		return logisticsServiceClient.listCenters().stream()
			.collect(Collectors.toMap(
				CentroAcopioResponseDto::id,
				CentroAcopioResponseDto::name,
				(left, right) -> left
			));
	}
}
