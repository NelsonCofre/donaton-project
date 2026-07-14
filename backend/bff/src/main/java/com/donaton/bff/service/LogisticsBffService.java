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

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		CollectionCenterResponse response = LogisticsMapper.toFrontend(created);
		log.info("BFF creó centro de acopio id={}", response.idCentro());
		return response;
	}

	public CollectionCenterResponse updateCenter(long id, CreateCollectionCenterRequest request) {
		var updated = logisticsServiceClient.updateCenter(id, LogisticsMapper.toServiceRequest(request));
		log.info("BFF actualizó centro de acopio id={}", id);
		return LogisticsMapper.toFrontend(updated);
	}

	public void deleteCenter(long id) {
		logisticsServiceClient.deleteCenter(id);
		log.info("BFF eliminó centro de acopio id={}", id);
	}

	public List<InventoryItemResponse> listInventory() {
		Map<Long, String> centerNames = loadCenterNames();
		List<InventoryItemResponse> items = logisticsServiceClient.listInventory().stream()
			.map(item -> LogisticsMapper.toFrontend(item, centerNames.get(item.centerId())))
			.toList();
		log.info("BFF listó {} inventarios", items.size());
		return items;
	}

	public InventoryItemResponse createInventory(CreateInventoryItemRequest request) {
		var created = logisticsServiceClient.createInventory(LogisticsMapper.toServiceRequest(request));
		InventoryItemResponse response = LogisticsMapper.toFrontend(created, loadCenterNames().get(created.centerId()));
		log.info("BFF creó inventario id={}", response.idInventario());
		return response;
	}

	public InventoryItemResponse updateInventory(long id, CreateInventoryItemRequest request) {
		var updated = logisticsServiceClient.updateInventory(id, LogisticsMapper.toServiceRequest(request));
		log.info("BFF actualizó inventario id={}", id);
		return LogisticsMapper.toFrontend(updated, loadCenterNames().get(updated.centerId()));
	}

	public void deleteInventory(long id) {
		logisticsServiceClient.deleteInventory(id);
		log.info("BFF eliminó inventario id={}", id);
	}

	public List<ShipmentResponse> listShipments() {
		Map<Long, String> centerNames = loadCenterNames();
		List<ShipmentResponse> shipments = logisticsServiceClient.listShipments().stream()
			.map(item -> LogisticsMapper.toFrontend(item, centerNames.get(item.centerId())))
			.toList();
		log.info("BFF listó {} envíos", shipments.size());
		return shipments;
	}

	public ShipmentResponse createShipment(CreateShipmentRequest request) {
		var created = logisticsServiceClient.createShipment(LogisticsMapper.toServiceRequest(request));
		ShipmentResponse response = LogisticsMapper.toFrontend(created, loadCenterNames().get(created.centerId()));
		log.info("BFF creó envío id={}", response.idEnvio());
		return response;
	}

	public ShipmentResponse updateShipment(long id, CreateShipmentRequest request) {
		var updated = logisticsServiceClient.updateShipment(id, LogisticsMapper.toServiceRequest(request));
		log.info("BFF actualizó envío id={}", id);
		return LogisticsMapper.toFrontend(updated, loadCenterNames().get(updated.centerId()));
	}

	public void deleteShipment(long id) {
		logisticsServiceClient.deleteShipment(id);
		log.info("BFF eliminó envío id={}", id);
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
