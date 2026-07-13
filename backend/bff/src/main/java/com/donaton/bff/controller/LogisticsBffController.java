package com.donaton.bff.controller;

import java.util.List;

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

import com.donaton.bff.dto.api.FrontendLogisticsDtos.CollectionCenterResponse;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.CreateCollectionCenterRequest;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.CreateInventoryItemRequest;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.CreateShipmentRequest;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.InventoryItemResponse;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.ShipmentResponse;
import com.donaton.bff.service.LogisticsBffService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Logística (Frontend)", description = "Centros, inventario y envíos adaptados para el frontend")
@Validated
@RestController
@RequestMapping("/api/v1/logistics")
public class LogisticsBffController {

	private final LogisticsBffService logisticsBffService;

	public LogisticsBffController(LogisticsBffService logisticsBffService) {
		this.logisticsBffService = logisticsBffService;
	}

	@GetMapping("/collection-centers")
	public List<CollectionCenterResponse> listCenters() {
		return logisticsBffService.listCenters();
	}

	@GetMapping("/collection-centers/{id}")
	public CollectionCenterResponse getCenterById(@PathVariable long id) {
		return logisticsBffService.getCenterById(id);
	}

	@PostMapping("/collection-centers")
	public ResponseEntity<CollectionCenterResponse> createCenter(
		@Valid @RequestBody CreateCollectionCenterRequest request
	) {
		return ResponseEntity.status(HttpStatus.CREATED).body(logisticsBffService.createCenter(request));
	}

	@PutMapping("/collection-centers/{id}")
	public CollectionCenterResponse updateCenter(
		@PathVariable long id,
		@Valid @RequestBody CreateCollectionCenterRequest request
	) {
		return logisticsBffService.updateCenter(id, request);
	}

	@DeleteMapping("/collection-centers/{id}")
	public ResponseEntity<Void> deleteCenter(@PathVariable long id) {
		logisticsBffService.deleteCenter(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/inventories")
	public List<InventoryItemResponse> listInventory() {
		return logisticsBffService.listInventory();
	}

	@PostMapping("/inventories")
	public ResponseEntity<InventoryItemResponse> createInventory(
		@Valid @RequestBody CreateInventoryItemRequest request
	) {
		return ResponseEntity.status(HttpStatus.CREATED).body(logisticsBffService.createInventory(request));
	}

	@PutMapping("/inventories/{id}")
	public InventoryItemResponse updateInventory(
		@PathVariable long id,
		@Valid @RequestBody CreateInventoryItemRequest request
	) {
		return logisticsBffService.updateInventory(id, request);
	}

	@DeleteMapping("/inventories/{id}")
	public ResponseEntity<Void> deleteInventory(@PathVariable long id) {
		logisticsBffService.deleteInventory(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/shipments")
	public List<ShipmentResponse> listShipments() {
		return logisticsBffService.listShipments();
	}

	@PostMapping("/shipments")
	public ResponseEntity<ShipmentResponse> createShipment(
		@Valid @RequestBody CreateShipmentRequest request
	) {
		return ResponseEntity.status(HttpStatus.CREATED).body(logisticsBffService.createShipment(request));
	}

	@PutMapping("/shipments/{id}")
	public ShipmentResponse updateShipment(
		@PathVariable long id,
		@Valid @RequestBody CreateShipmentRequest request
	) {
		return logisticsBffService.updateShipment(id, request);
	}

	@DeleteMapping("/shipments/{id}")
	public ResponseEntity<Void> deleteShipment(@PathVariable long id) {
		logisticsBffService.deleteShipment(id);
		return ResponseEntity.noContent().build();
	}
}
