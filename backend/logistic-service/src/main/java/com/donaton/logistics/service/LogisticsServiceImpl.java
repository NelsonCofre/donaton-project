package com.donaton.logistics.service;

import com.donaton.logistics.dto.CentroAcopioRequestDto;
import com.donaton.logistics.dto.CentroAcopioResponseDto;
import com.donaton.logistics.dto.EnvioRequestDto;
import com.donaton.logistics.dto.EnvioResponseDto;
import com.donaton.logistics.dto.InventarioRequestDto;
import com.donaton.logistics.dto.InventarioResponseDto;
import com.donaton.logistics.exception.ResourceNotFoundException;
import com.donaton.logistics.model.CentroAcopio;
import com.donaton.logistics.model.Envio;
import com.donaton.logistics.model.Inventario;
import com.donaton.logistics.repository.CentroAcopioRepository;
import com.donaton.logistics.repository.EnvioRepository;
import com.donaton.logistics.repository.InventarioRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogisticsServiceImpl implements LogisticsService {

	private final CentroAcopioRepository centroAcopioRepository;
	private final InventarioRepository inventarioRepository;
	private final EnvioRepository envioRepository;

	@Override
	@Transactional
	public CentroAcopioResponseDto createCentroAcopio(CentroAcopioRequestDto request) {
		CentroAcopio entity = toCentroAcopioEntity(request);
		return toCentroAcopioResponse(centroAcopioRepository.save(entity));
	}

	@Override
	@Transactional(readOnly = true)
	public List<CentroAcopioResponseDto> findAllCentrosAcopio() {
		return centroAcopioRepository.findAll().stream().map(this::toCentroAcopioResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public CentroAcopioResponseDto findCentroAcopioById(Long id) {
		return centroAcopioRepository.findById(id)
				.map(this::toCentroAcopioResponse)
				.orElseThrow(() -> new ResourceNotFoundException("Centro de acopio no encontrado: " + id));
	}

	@Override
	@Transactional
	public CentroAcopioResponseDto updateCentroAcopio(Long id, CentroAcopioRequestDto request) {
		CentroAcopio existing = centroAcopioRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Centro de acopio no encontrado: " + id));
		existing.setName(request.getName());
		existing.setLocation(request.getLocation());
		return toCentroAcopioResponse(centroAcopioRepository.save(existing));
	}

	@Override
	@Transactional
	public void deleteCentroAcopioById(Long id) {
		if (!centroAcopioRepository.existsById(id)) {
			throw new ResourceNotFoundException("Centro de acopio no encontrado: " + id);
		}
		centroAcopioRepository.deleteById(id);
	}

	@Override
	@Transactional
	public InventarioResponseDto createInventario(InventarioRequestDto request) {
		ensureCentroAcopioExists(request.getCenterId());
		Inventario entity = toInventarioEntity(request);
		return toInventarioResponse(inventarioRepository.save(entity));
	}

	@Override
	@Transactional(readOnly = true)
	public List<InventarioResponseDto> findAllInventarios() {
		return inventarioRepository.findAll().stream().map(this::toInventarioResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public InventarioResponseDto findInventarioById(Long id) {
		return inventarioRepository.findById(id)
				.map(this::toInventarioResponse)
				.orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado: " + id));
	}

	@Override
	@Transactional
	public InventarioResponseDto updateInventario(Long id, InventarioRequestDto request) {
		ensureCentroAcopioExists(request.getCenterId());
		Inventario existing = inventarioRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado: " + id));
		existing.setCenterId(request.getCenterId());
		existing.setResource(request.getResource());
		existing.setQuantity(request.getQuantity());
		return toInventarioResponse(inventarioRepository.save(existing));
	}

	@Override
	@Transactional
	public void deleteInventarioById(Long id) {
		if (!inventarioRepository.existsById(id)) {
			throw new ResourceNotFoundException("Inventario no encontrado: " + id);
		}
		inventarioRepository.deleteById(id);
	}

	@Override
	@Transactional
	public EnvioResponseDto createEnvio(EnvioRequestDto request) {
		ensureCentroAcopioExists(request.getCenterId());
		Envio entity = toEnvioEntity(request);
		return toEnvioResponse(envioRepository.save(entity));
	}

	@Override
	@Transactional(readOnly = true)
	public List<EnvioResponseDto> findAllEnvios() {
		return envioRepository.findAll().stream().map(this::toEnvioResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public EnvioResponseDto findEnvioById(Long id) {
		return envioRepository.findById(id)
				.map(this::toEnvioResponse)
				.orElseThrow(() -> new ResourceNotFoundException("Envío no encontrado: " + id));
	}

	@Override
	@Transactional
	public EnvioResponseDto updateEnvio(Long id, EnvioRequestDto request) {
		ensureCentroAcopioExists(request.getCenterId());
		Envio existing = envioRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Envío no encontrado: " + id));
		existing.setDate(request.getDate());
		existing.setStatus(request.getStatus());
		existing.setCenterId(request.getCenterId());
		return toEnvioResponse(envioRepository.save(existing));
	}

	@Override
	@Transactional
	public void deleteEnvioById(Long id) {
		if (!envioRepository.existsById(id)) {
			throw new ResourceNotFoundException("Envío no encontrado: " + id);
		}
		envioRepository.deleteById(id);
	}

	private void ensureCentroAcopioExists(Long centerId) {
		if (!centroAcopioRepository.existsById(centerId)) {
			throw new ResourceNotFoundException("Centro de acopio no encontrado: " + centerId);
		}
	}

	private CentroAcopio toCentroAcopioEntity(CentroAcopioRequestDto request) {
		return CentroAcopio.builder().name(request.getName()).location(request.getLocation()).build();
	}

	private CentroAcopioResponseDto toCentroAcopioResponse(CentroAcopio entity) {
		return CentroAcopioResponseDto.builder()
				.id(entity.getId())
				.name(entity.getName())
				.location(entity.getLocation())
				.build();
	}

	private Inventario toInventarioEntity(InventarioRequestDto request) {
		return Inventario.builder()
				.centerId(request.getCenterId())
				.resource(request.getResource())
				.quantity(request.getQuantity())
				.build();
	}

	private InventarioResponseDto toInventarioResponse(Inventario entity) {
		return InventarioResponseDto.builder()
				.id(entity.getId())
				.centerId(entity.getCenterId())
				.resource(entity.getResource())
				.quantity(entity.getQuantity())
				.build();
	}

	private Envio toEnvioEntity(EnvioRequestDto request) {
		return Envio.builder()
				.date(request.getDate())
				.status(request.getStatus())
				.centerId(request.getCenterId())
				.build();
	}

	private EnvioResponseDto toEnvioResponse(Envio entity) {
		return EnvioResponseDto.builder()
				.id(entity.getId())
				.date(entity.getDate())
				.status(entity.getStatus())
				.centerId(entity.getCenterId())
				.build();
	}
}
