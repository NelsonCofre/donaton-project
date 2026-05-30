package com.donaton.logistic.service;

import com.donaton.logistic.dto.CollectionCenterRequestDto;
import com.donaton.logistic.dto.CollectionCenterResponseDto;
import com.donaton.logistic.dto.InventoryRequestDto;
import com.donaton.logistic.dto.InventoryResponseDto;
import com.donaton.logistic.dto.ShipmentRequestDto;
import com.donaton.logistic.dto.ShipmentResponseDto;
import java.util.List;

public interface LogisticService {

	CollectionCenterResponseDto createCollectionCenter(CollectionCenterRequestDto request);

	List<CollectionCenterResponseDto> findAllCollectionCenters();

	CollectionCenterResponseDto findCollectionCenterById(Long id);

	CollectionCenterResponseDto updateCollectionCenter(Long id, CollectionCenterRequestDto request);

	void deleteCollectionCenterById(Long id);

	InventoryResponseDto createInventory(InventoryRequestDto request);

	List<InventoryResponseDto> findAllInventories();

	InventoryResponseDto findInventoryById(Long id);

	InventoryResponseDto updateInventory(Long id, InventoryRequestDto request);

	void deleteInventoryById(Long id);

	ShipmentResponseDto createShipment(ShipmentRequestDto request);

	List<ShipmentResponseDto> findAllShipments();

	ShipmentResponseDto findShipmentById(Long id);

	ShipmentResponseDto updateShipment(Long id, ShipmentRequestDto request);

	void deleteShipmentById(Long id);
}
