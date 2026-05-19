package com.donaton.donation.controller;

import com.donaton.donation.dto.DonationRequestDto;
import com.donaton.donation.dto.DonationResponseDto;
import com.donaton.donation.service.DonationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/donations")
@RequiredArgsConstructor
public class DonationController {

	private final DonationService donationService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public DonationResponseDto create(@Valid @RequestBody DonationRequestDto request) {
		return donationService.create(request);
	}

	@GetMapping
	public List<DonationResponseDto> list() {
		return donationService.findAll();
	}

	@GetMapping("/{id}")
	public DonationResponseDto getById(@PathVariable Long id) {
		return donationService.findById(id);
	}

	@PutMapping("/{id}")
	public DonationResponseDto update(@PathVariable Long id, @Valid @RequestBody DonationRequestDto request) {
		return donationService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		donationService.deleteById(id);
	}
}
