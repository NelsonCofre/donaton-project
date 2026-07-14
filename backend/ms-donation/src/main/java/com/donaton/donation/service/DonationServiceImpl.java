package com.donaton.donation.service;

import com.donaton.donation.dto.DonationRequestDto;
import com.donaton.donation.dto.DonationResponseDto;
import com.donaton.donation.exception.ResourceNotFoundException;
import com.donaton.donation.model.Donation;
import com.donaton.donation.repository.DonationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DonationServiceImpl implements DonationService {

	private final DonationRepository donationRepository;

	@Override
	@Transactional
	public DonationResponseDto create(DonationRequestDto request) {
		Donation entity = toEntity(request);
		DonationResponseDto response = toResponse(donationRepository.save(entity));
		log.info("Donación creada id={} recurso={} cantidad={}", response.getId(), response.getResourceName(), response.getQuantity());
		return response;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DonationResponseDto> findAll() {
		return donationRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public DonationResponseDto findById(Long id) {
		return donationRepository.findById(id).map(this::toResponse).orElseThrow(() -> new ResourceNotFoundException("Donación no encontrada: " + id));
	}

	@Override
	@Transactional
	public DonationResponseDto update(Long id, DonationRequestDto request) {
		Donation existing = donationRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Donación no encontrada: " + id));
		existing.setResourceName(request.getResourceName());
		existing.setQuantity(request.getQuantity());
		existing.setOrigin(request.getOrigin());
		existing.setDonationDate(request.getDonationDate());
		existing.setWarehouseName(request.getWarehouseName());
		DonationResponseDto response = toResponse(donationRepository.save(existing));
		log.info("Donación actualizada id={}", id);
		return response;
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		if (!donationRepository.existsById(id)) {
			throw new ResourceNotFoundException("Donación no encontrada: " + id);
		}
		donationRepository.deleteById(id);
		log.info("Donación eliminada id={}", id);
	}

	private Donation toEntity(DonationRequestDto request) {
		return Donation.builder()
				.resourceName(request.getResourceName())
				.quantity(request.getQuantity())
				.origin(request.getOrigin())
				.donationDate(request.getDonationDate())
				.warehouseName(request.getWarehouseName())
				.build();
	}

	private DonationResponseDto toResponse(Donation entity) {
		return DonationResponseDto.builder()
				.id(entity.getId())
				.resourceName(entity.getResourceName())
				.quantity(entity.getQuantity())
				.origin(entity.getOrigin())
				.donationDate(entity.getDonationDate())
				.warehouseName(entity.getWarehouseName())
				.build();
	}
}
