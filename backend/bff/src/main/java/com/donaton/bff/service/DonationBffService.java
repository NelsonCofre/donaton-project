package com.donaton.bff.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.donaton.bff.client.DonationServiceClient;
import com.donaton.bff.dto.api.FrontendDonationDtos.CreateDonacionRequest;
import com.donaton.bff.dto.api.FrontendDonationDtos.DonacionResponse;
import com.donaton.bff.mapper.DonationMapper;

@Service
public class DonationBffService {

	private final DonationServiceClient donationServiceClient;

	public DonationBffService(DonationServiceClient donationServiceClient) {
		this.donationServiceClient = donationServiceClient;
	}

	public List<DonacionResponse> list(String authorization) {
		return donationServiceClient.list(authorization).stream()
			.map(DonationMapper::toFrontend)
			.toList();
	}

	public DonacionResponse getById(long id, String authorization) {
		return DonationMapper.toFrontend(donationServiceClient.getById(id, authorization));
	}

	public DonacionResponse create(CreateDonacionRequest request, String authorization) {
		var created = donationServiceClient.create(
			DonationMapper.toServiceRequest(request),
			authorization
		);
		return DonationMapper.toFrontend(created);
	}

	public DonacionResponse update(long id, CreateDonacionRequest request, String authorization) {
		var updated = donationServiceClient.update(
			id,
			DonationMapper.toServiceRequest(request),
			authorization
		);
		return DonationMapper.toFrontend(updated);
	}

	public void delete(long id, String authorization) {
		donationServiceClient.delete(id, authorization);
	}
}
