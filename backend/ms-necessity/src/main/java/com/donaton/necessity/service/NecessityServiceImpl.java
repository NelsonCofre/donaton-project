package com.donaton.necessity.service;

import com.donaton.necessity.dto.NecessityRequestDto;
import com.donaton.necessity.dto.NecessityResponseDto;
import com.donaton.necessity.exception.ResourceNotFoundException;
import com.donaton.necessity.model.Necessity;
import com.donaton.necessity.repository.NecessityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NecessityServiceImpl implements NecessityService {

	private final NecessityRepository necessityRepository;

	@Override
	@Transactional
	public NecessityResponseDto create(NecessityRequestDto request) {
		Necessity entity = toEntity(request);
		return toResponse(necessityRepository.save(entity));
	}

	@Override
	@Transactional(readOnly = true)
	public List<NecessityResponseDto> findAll() {
		return necessityRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public NecessityResponseDto findById(Long id) {
		return necessityRepository.findById(id)
				.map(this::toResponse)
				.orElseThrow(() -> new ResourceNotFoundException("Necesidad no encontrada: " + id));
	}

	@Override
	@Transactional
	public NecessityResponseDto update(Long id, NecessityRequestDto request) {
		Necessity existing = necessityRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Necesidad no encontrada: " + id));
		existing.setResourceName(request.getResourceName());
		existing.setQuantity(request.getQuantity());
		existing.setLocation(request.getLocation());
		existing.setReportedDate(request.getReportedDate());
		existing.setReportedBy(request.getReportedBy());
		return toResponse(necessityRepository.save(existing));
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		if (!necessityRepository.existsById(id)) {
			throw new ResourceNotFoundException("Necesidad no encontrada: " + id);
		}
		necessityRepository.deleteById(id);
	}

	private Necessity toEntity(NecessityRequestDto request) {
		return Necessity.builder()
				.resourceName(request.getResourceName())
				.quantity(request.getQuantity())
				.location(request.getLocation())
				.reportedDate(request.getReportedDate())
				.reportedBy(request.getReportedBy())
				.build();
	}

	private NecessityResponseDto toResponse(Necessity entity) {
		return NecessityResponseDto.builder()
				.id(entity.getId())
				.resourceName(entity.getResourceName())
				.quantity(entity.getQuantity())
				.location(entity.getLocation())
				.reportedDate(entity.getReportedDate())
				.reportedBy(entity.getReportedBy())
				.build();
	}
}
