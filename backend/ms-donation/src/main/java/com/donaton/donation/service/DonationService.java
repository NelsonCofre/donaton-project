package com.donaton.donation.service;

import com.donaton.donation.dto.DonationRequestDto;
import com.donaton.donation.dto.DonationResponseDto;
import java.util.List;

public interface DonationService {

	DonationResponseDto create(DonationRequestDto request);

	List<DonationResponseDto> findAll();

	DonationResponseDto findById(Long id);

	DonationResponseDto update(Long id, DonationRequestDto request);

	void deleteById(Long id);
}
