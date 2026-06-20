package com.donaton.necessity.service;

import com.donaton.necessity.dto.NecessityRequestDto;
import com.donaton.necessity.dto.NecessityResponseDto;
import java.util.List;

public interface NecessityService {

	NecessityResponseDto create(NecessityRequestDto request);

	List<NecessityResponseDto> findAll();

	NecessityResponseDto findById(Long id);

	NecessityResponseDto update(Long id, NecessityRequestDto request);

	void deleteById(Long id);
}
