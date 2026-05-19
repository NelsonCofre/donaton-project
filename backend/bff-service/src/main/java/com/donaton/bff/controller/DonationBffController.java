package com.donaton.bff.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.donaton.bff.client.DonationServiceClient;
import com.donaton.bff.dto.api.FrontendDonationDtos.CreateDonacionRequest;
import com.donaton.bff.dto.api.FrontendDonationDtos.DonacionResponse;
import com.donaton.bff.mapper.DonationMapper;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/donations")
public class DonationBffController {

	private final DonationServiceClient donationServiceClient;

	public DonationBffController(DonationServiceClient donationServiceClient) {
		this.donationServiceClient = donationServiceClient;
	}

	@GetMapping
	public List<DonacionResponse> list(
		@RequestHeader(value = "Authorization", required = false) String authorization
	) {
		return donationServiceClient.list(authorization).stream()
			.map(DonationMapper::toFrontend)
			.toList();
	}

	@GetMapping("/{id}")
	public DonacionResponse getById(
		@PathVariable long id,
		@RequestHeader(value = "Authorization", required = false) String authorization
	) {
		return DonationMapper.toFrontend(donationServiceClient.getById(id, authorization));
	}

	@PostMapping
	public ResponseEntity<DonacionResponse> create(
		@Valid @RequestBody CreateDonacionRequest request,
		@RequestHeader(value = "Authorization", required = false) String authorization
	) {
		var created = donationServiceClient.create(
			DonationMapper.toServiceRequest(request),
			authorization
		);
		return ResponseEntity.status(HttpStatus.CREATED).body(DonationMapper.toFrontend(created));
	}

	@PutMapping("/{id}")
	public DonacionResponse update(
		@PathVariable long id,
		@Valid @RequestBody CreateDonacionRequest request,
		@RequestHeader(value = "Authorization", required = false) String authorization
	) {
		var updated = donationServiceClient.update(
			id,
			DonationMapper.toServiceRequest(request),
			authorization
		);
		return DonationMapper.toFrontend(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(
		@PathVariable long id,
		@RequestHeader(value = "Authorization", required = false) String authorization
	) {
		donationServiceClient.delete(id, authorization);
		return ResponseEntity.noContent().build();
	}
}
