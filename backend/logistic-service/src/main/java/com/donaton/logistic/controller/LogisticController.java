package com.donaton.logistic.controller;

import com.donaton.logistic.dto.CollectionCenterRequestDto;
import com.donaton.logistic.dto.CollectionCenterResponseDto;
import com.donaton.logistic.dto.InventoryRequestDto;
import com.donaton.logistic.dto.InventoryResponseDto;
import com.donaton.logistic.dto.ShipmentRequestDto;
import com.donaton.logistic.dto.ShipmentResponseDto;
import com.donaton.logistic.service.LogisticService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/logistics")
@RequiredArgsConstructor
public class LogisticController {

	private final LogisticService logisticService;

	@PostMapping("/collection-centers")
	@ResponseStatus(HttpStatus.CREATED)
	public CollectionCenterResponseDto createCollectionCenter(@Valid @RequestBody CollectionCenterRequestDto request) {
		return logisticService.createCollectionCenter(request);
	}

	@GetMapping("/collection-centers")
	public List<CollectionCenterResponseDto> listCollectionCenters() {
		return logisticService.findAllCollectionCenters();
	}

	@GetMapping("/collection-centers/{id}")
	public CollectionCenterResponseDto getCollectionCenterById(@PathVariable Long id) {
		return logisticService.findCollectionCenterById(id);
	}

	@PutMapping("/collection-centers/{id}")
	public CollectionCenterResponseDto updateCollectionCenter(
		@PathVariable Long id,
		@Valid @RequestBody CollectionCenterRequestDto request
	) {
		return logisticService.updateCollectionCenter(id, request);
	}

	@DeleteMapping("/collection-centers/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCollectionCenter(@PathVariable Long id) {
		logisticService.deleteCollectionCenterById(id);
	}

	@PostMapping("/inventories")
	@ResponseStatus(HttpStatus.CREATED)
	public InventoryResponseDto createInventory(@Valid @RequestBody InventoryRequestDto request) {
		return logisticService.createInventory(request);
	}

	@GetMapping("/inventories")
	public List<InventoryResponseDto> listInventories() {
		return logisticService.findAllInventories();
	}

	@GetMapping("/inventories/{id}")
	public InventoryResponseDto getInventoryById(@PathVariable Long id) {
		return logisticService.findInventoryById(id);
	}

	@PutMapping("/inventories/{id}")
	public InventoryResponseDto updateInventory(
		@PathVariable Long id,
		@Valid @RequestBody InventoryRequestDto request
	) {
		return logisticService.updateInventory(id, request);
	}

	@DeleteMapping("/inventories/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteInventory(@PathVariable Long id) {
		logisticService.deleteInventoryById(id);
	}

	@PostMapping("/shipments")
	@ResponseStatus(HttpStatus.CREATED)
	public ShipmentResponseDto createShipment(@Valid @RequestBody ShipmentRequestDto request) {
		return logisticService.createShipment(request);
	}

	@GetMapping("/shipments")
	public List<ShipmentResponseDto> listShipments() {
		return logisticService.findAllShipments();
	}

	@GetMapping("/shipments/{id}")
	public ShipmentResponseDto getShipmentById(@PathVariable Long id) {
		return logisticService.findShipmentById(id);
	}

	@PutMapping("/shipments/{id}")
	public ShipmentResponseDto updateShipment(
		@PathVariable Long id,
		@Valid @RequestBody ShipmentRequestDto request
	) {
		return logisticService.updateShipment(id, request);
	}

	@DeleteMapping("/shipments/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteShipment(@PathVariable Long id) {
		logisticService.deleteShipmentById(id);
	}
}
