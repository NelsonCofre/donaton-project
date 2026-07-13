package com.donaton.bff.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.donaton.bff.client.LogisticsServiceClient;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.CollectionCenterResponse;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.CreateCollectionCenterRequest;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.CreateInventoryItemRequest;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.CreateShipmentRequest;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.InventoryItemResponse;
import com.donaton.bff.dto.api.FrontendLogisticsDtos.ShipmentResponse;
import com.donaton.bff.dto.logistics.LogisticsServiceDtos.CentroAcopioResponseDto;
import com.donaton.bff.mapper.LogisticsMapper;

@Service
public class LogisticsBffService {

	private final LogisticsServiceClient logisticsServiceClient;

	public LogisticsBffService(LogisticsServiceClient logisticsServiceClient) {
		this.logisticsServiceClient = logisticsServiceClient;
	}

	public List<CollectionCenterResponse> listCenters() {
		return logisticsServiceClient.listCenters().stream()
			.map(LogisticsMapper::toFrontend)
			.toList();
	}

	public CollectionCenterResponse getCenterById(long id) {
		return LogisticsMapper.toFrontend(logisticsServiceClient.getCenterById(id));
	}

	public CollectionCenterResponse createCenter(CreateCollectionCenterRequest request) {
		var created = logisticsServiceClient.createCenter(LogisticsMapper.toServiceRequest(request));
		return LogisticsMapper.toFrontend(created);
	}

	public CollectionCenterResponse updateCenter(long id, CreateCollectionCenterRequest request) {
		var updated = logisticsServiceClient.updateCenter(id, LogisticsMapper.toServiceRequest(request));
		return LogisticsMapper.toFrontend(updated);
	}

	public void deleteCenter(long id) {
		logisticsServiceClient.deleteCenter(id);
	}

	public List<InventoryItemResponse> listInventory() {
		Map<Long, String> centerNames = loadCenterNames();
		return logisticsServiceClient.listInventory().stream()
			.map(item -> LogisticsMapper.toFrontend(item, centerNames.get(item.centerId())))
			.toList();
	}

	public InventoryItemResponse createInventory(CreateInventoryItemRequest request) {
		var created = logisticsServiceClient.createInventory(LogisticsMapper.toServiceRequest(request));
		return LogisticsMapper.toFrontend(created, loadCenterNames().get(created.centerId()));
	}

	public InventoryItemResponse updateInventory(long id, CreateInventoryItemRequest request) {
		var updated = logisticsServiceClient.updateInventory(id, LogisticsMapper.toServiceRequest(request));
		return LogisticsMapper.toFrontend(updated, loadCenterNames().get(updated.centerId()));
	}

	public void deleteInventory(long id) {
		logisticsServiceClient.deleteInventory(id);
	}

	public List<ShipmentResponse> listShipments() {
		Map<Long, String> centerNames = loadCenterNames();
		return logisticsServiceClient.listShipments().stream()
			.map(item -> LogisticsMapper.toFrontend(item, centerNames.get(item.centerId())))
			.toList();
	}

	public ShipmentResponse createShipment(CreateShipmentRequest request) {
		var created = logisticsServiceClient.createShipment(LogisticsMapper.toServiceRequest(request));
		return LogisticsMapper.toFrontend(created, loadCenterNames().get(created.centerId()));
	}

	public ShipmentResponse updateShipment(long id, CreateShipmentRequest request) {
		var updated = logisticsServiceClient.updateShipment(id, LogisticsMapper.toServiceRequest(request));
		return LogisticsMapper.toFrontend(updated, loadCenterNames().get(updated.centerId()));
	}

	public void deleteShipment(long id) {
		logisticsServiceClient.deleteShipment(id);
	}

	private Map<Long, String> loadCenterNames() {
		return logisticsServiceClient.listCenters().stream()
			.collect(Collectors.toMap(
				CentroAcopioResponseDto::id,
				CentroAcopioResponseDto::name,
				(left, right) -> left
			));
	}
}
