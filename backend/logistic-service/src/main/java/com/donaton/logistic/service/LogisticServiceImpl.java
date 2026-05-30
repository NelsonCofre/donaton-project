package com.donaton.logistic.service;

import com.donaton.logistic.dto.CollectionCenterRequestDto;
import com.donaton.logistic.dto.CollectionCenterResponseDto;
import com.donaton.logistic.dto.InventoryRequestDto;
import com.donaton.logistic.dto.InventoryResponseDto;
import com.donaton.logistic.dto.ShipmentRequestDto;
import com.donaton.logistic.dto.ShipmentResponseDto;
import com.donaton.logistic.exception.ResourceNotFoundException;
import com.donaton.logistic.model.CollectionCenter;
import com.donaton.logistic.model.Inventory;
import com.donaton.logistic.model.Shipment;
import com.donaton.logistic.repository.CollectionCenterRepository;
import com.donaton.logistic.repository.InventoryRepository;
import com.donaton.logistic.repository.ShipmentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogisticServiceImpl implements LogisticService {

	private final CollectionCenterRepository collectionCenterRepository;
	private final InventoryRepository inventoryRepository;
	private final ShipmentRepository shipmentRepository;

	@Override
	@Transactional
	public CollectionCenterResponseDto createCollectionCenter(CollectionCenterRequestDto request) {
		CollectionCenter entity = toCollectionCenterEntity(request);
		return toCollectionCenterResponse(collectionCenterRepository.save(entity));
	}

	@Override
	@Transactional(readOnly = true)
	public List<CollectionCenterResponseDto> findAllCollectionCenters() {
		return collectionCenterRepository.findAll().stream().map(this::toCollectionCenterResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public CollectionCenterResponseDto findCollectionCenterById(Long id) {
		return collectionCenterRepository.findById(id)
				.map(this::toCollectionCenterResponse)
				.orElseThrow(() -> new ResourceNotFoundException("Centro de acopio no encontrado: " + id));
	}

	@Override
	@Transactional
	public CollectionCenterResponseDto updateCollectionCenter(Long id, CollectionCenterRequestDto request) {
		CollectionCenter existing = collectionCenterRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Centro de acopio no encontrado: " + id));
		existing.setName(request.getName());
		existing.setLocation(request.getLocation());
		return toCollectionCenterResponse(collectionCenterRepository.save(existing));
	}

	@Override
	@Transactional
	public void deleteCollectionCenterById(Long id) {
		if (!collectionCenterRepository.existsById(id)) {
			throw new ResourceNotFoundException("Centro de acopio no encontrado: " + id);
		}
		collectionCenterRepository.deleteById(id);
	}

	@Override
	@Transactional
	public InventoryResponseDto createInventory(InventoryRequestDto request) {
		ensureCollectionCenterExists(request.getCenterId());
		Inventory entity = toInventoryEntity(request);
		return toInventoryResponse(inventoryRepository.save(entity));
	}

	@Override
	@Transactional(readOnly = true)
	public List<InventoryResponseDto> findAllInventories() {
		return inventoryRepository.findAll().stream().map(this::toInventoryResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public InventoryResponseDto findInventoryById(Long id) {
		return inventoryRepository.findById(id)
				.map(this::toInventoryResponse)
				.orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado: " + id));
	}

	@Override
	@Transactional
	public InventoryResponseDto updateInventory(Long id, InventoryRequestDto request) {
		ensureCollectionCenterExists(request.getCenterId());
		Inventory existing = inventoryRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado: " + id));
		existing.setCenterId(request.getCenterId());
		existing.setResource(request.getResource());
		existing.setQuantity(request.getQuantity());
		return toInventoryResponse(inventoryRepository.save(existing));
	}

	@Override
	@Transactional
	public void deleteInventoryById(Long id) {
		if (!inventoryRepository.existsById(id)) {
			throw new ResourceNotFoundException("Inventario no encontrado: " + id);
		}
		inventoryRepository.deleteById(id);
	}

	@Override
	@Transactional
	public ShipmentResponseDto createShipment(ShipmentRequestDto request) {
		ensureCollectionCenterExists(request.getCenterId());
		Shipment entity = toShipmentEntity(request);
		return toShipmentResponse(shipmentRepository.save(entity));
	}

	@Override
	@Transactional(readOnly = true)
	public List<ShipmentResponseDto> findAllShipments() {
		return shipmentRepository.findAll().stream().map(this::toShipmentResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public ShipmentResponseDto findShipmentById(Long id) {
		return shipmentRepository.findById(id)
				.map(this::toShipmentResponse)
				.orElseThrow(() -> new ResourceNotFoundException("Envío no encontrado: " + id));
	}

	@Override
	@Transactional
	public ShipmentResponseDto updateShipment(Long id, ShipmentRequestDto request) {
		ensureCollectionCenterExists(request.getCenterId());
		Shipment existing = shipmentRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Envío no encontrado: " + id));
		existing.setDate(request.getDate());
		existing.setStatus(request.getStatus());
		existing.setCenterId(request.getCenterId());
		return toShipmentResponse(shipmentRepository.save(existing));
	}

	@Override
	@Transactional
	public void deleteShipmentById(Long id) {
		if (!shipmentRepository.existsById(id)) {
			throw new ResourceNotFoundException("Envío no encontrado: " + id);
		}
		shipmentRepository.deleteById(id);
	}

	private void ensureCollectionCenterExists(Long centerId) {
		if (!collectionCenterRepository.existsById(centerId)) {
			throw new ResourceNotFoundException("Centro de acopio no encontrado: " + centerId);
		}
	}

	private CollectionCenter toCollectionCenterEntity(CollectionCenterRequestDto request) {
		return CollectionCenter.builder().name(request.getName()).location(request.getLocation()).build();
	}

	private CollectionCenterResponseDto toCollectionCenterResponse(CollectionCenter entity) {
		return CollectionCenterResponseDto.builder()
				.id(entity.getId())
				.name(entity.getName())
				.location(entity.getLocation())
				.build();
	}

	private Inventory toInventoryEntity(InventoryRequestDto request) {
		return Inventory.builder()
				.centerId(request.getCenterId())
				.resource(request.getResource())
				.quantity(request.getQuantity())
				.build();
	}

	private InventoryResponseDto toInventoryResponse(Inventory entity) {
		return InventoryResponseDto.builder()
				.id(entity.getId())
				.centerId(entity.getCenterId())
				.resource(entity.getResource())
				.quantity(entity.getQuantity())
				.build();
	}

	private Shipment toShipmentEntity(ShipmentRequestDto request) {
		return Shipment.builder()
				.date(request.getDate())
				.status(request.getStatus())
				.centerId(request.getCenterId())
				.build();
	}

	private ShipmentResponseDto toShipmentResponse(Shipment entity) {
		return ShipmentResponseDto.builder()
				.id(entity.getId())
				.date(entity.getDate())
				.status(entity.getStatus())
				.centerId(entity.getCenterId())
				.build();
	}
}
