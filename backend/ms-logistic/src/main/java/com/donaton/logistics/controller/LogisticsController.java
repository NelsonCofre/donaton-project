package com.donaton.logistics.controller;

import com.donaton.logistics.dto.CentroAcopioRequestDto;
import com.donaton.logistics.dto.CentroAcopioResponseDto;
import com.donaton.logistics.dto.EnvioRequestDto;
import com.donaton.logistics.dto.EnvioResponseDto;
import com.donaton.logistics.dto.InventarioRequestDto;
import com.donaton.logistics.dto.InventarioResponseDto;
import com.donaton.logistics.service.LogisticsService;
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
public class LogisticsController {

	private final LogisticsService logisticsService;

	@PostMapping("/collection-centers")
	@ResponseStatus(HttpStatus.CREATED)
	public CentroAcopioResponseDto createCollectionCenter(@Valid @RequestBody CentroAcopioRequestDto request) {
		return logisticsService.createCentroAcopio(request);
	}

	@GetMapping("/collection-centers")
	public List<CentroAcopioResponseDto> listCollectionCenters() {
		return logisticsService.findAllCentrosAcopio();
	}

	@GetMapping("/collection-centers/{id}")
	public CentroAcopioResponseDto getCollectionCenterById(@PathVariable Long id) {
		return logisticsService.findCentroAcopioById(id);
	}

	@PutMapping("/collection-centers/{id}")
	public CentroAcopioResponseDto updateCollectionCenter(
		@PathVariable Long id,
		@Valid @RequestBody CentroAcopioRequestDto request
	) {
		return logisticsService.updateCentroAcopio(id, request);
	}

	@DeleteMapping("/collection-centers/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCollectionCenter(@PathVariable Long id) {
		logisticsService.deleteCentroAcopioById(id);
	}

	@PostMapping("/inventories")
	@ResponseStatus(HttpStatus.CREATED)
	public InventarioResponseDto createInventory(@Valid @RequestBody InventarioRequestDto request) {
		return logisticsService.createInventario(request);
	}

	@GetMapping("/inventories")
	public List<InventarioResponseDto> listInventories() {
		return logisticsService.findAllInventarios();
	}

	@GetMapping("/inventories/{id}")
	public InventarioResponseDto getInventoryById(@PathVariable Long id) {
		return logisticsService.findInventarioById(id);
	}

	@PutMapping("/inventories/{id}")
	public InventarioResponseDto updateInventory(
		@PathVariable Long id,
		@Valid @RequestBody InventarioRequestDto request
	) {
		return logisticsService.updateInventario(id, request);
	}

	@DeleteMapping("/inventories/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteInventory(@PathVariable Long id) {
		logisticsService.deleteInventarioById(id);
	}

	@PostMapping("/shipments")
	@ResponseStatus(HttpStatus.CREATED)
	public EnvioResponseDto createShipment(@Valid @RequestBody EnvioRequestDto request) {
		return logisticsService.createEnvio(request);
	}

	@GetMapping("/shipments")
	public List<EnvioResponseDto> listShipments() {
		return logisticsService.findAllEnvios();
	}

	@GetMapping("/shipments/{id}")
	public EnvioResponseDto getShipmentById(@PathVariable Long id) {
		return logisticsService.findEnvioById(id);
	}

	@PutMapping("/shipments/{id}")
	public EnvioResponseDto updateShipment(
		@PathVariable Long id,
		@Valid @RequestBody EnvioRequestDto request
	) {
		return logisticsService.updateEnvio(id, request);
	}

	@DeleteMapping("/shipments/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteShipment(@PathVariable Long id) {
		logisticsService.deleteEnvioById(id);
	}
}
