package com.donaton.logistics.service;

import com.donaton.logistics.dto.CentroAcopioRequestDto;
import com.donaton.logistics.dto.CentroAcopioResponseDto;
import com.donaton.logistics.dto.EnvioRequestDto;
import com.donaton.logistics.dto.EnvioResponseDto;
import com.donaton.logistics.dto.InventarioRequestDto;
import com.donaton.logistics.dto.InventarioResponseDto;
import java.util.List;

public interface LogisticsService {

	CentroAcopioResponseDto createCentroAcopio(CentroAcopioRequestDto request);

	List<CentroAcopioResponseDto> findAllCentrosAcopio();

	CentroAcopioResponseDto findCentroAcopioById(Long id);

	CentroAcopioResponseDto updateCentroAcopio(Long id, CentroAcopioRequestDto request);

	void deleteCentroAcopioById(Long id);

	InventarioResponseDto createInventario(InventarioRequestDto request);

	List<InventarioResponseDto> findAllInventarios();

	InventarioResponseDto findInventarioById(Long id);

	InventarioResponseDto updateInventario(Long id, InventarioRequestDto request);

	void deleteInventarioById(Long id);

	EnvioResponseDto createEnvio(EnvioRequestDto request);

	List<EnvioResponseDto> findAllEnvios();

	EnvioResponseDto findEnvioById(Long id);

	EnvioResponseDto updateEnvio(Long id, EnvioRequestDto request);

	void deleteEnvioById(Long id);
}
