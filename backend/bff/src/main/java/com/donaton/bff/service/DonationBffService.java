package com.donaton.bff.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.donaton.bff.client.DonationServiceClient;
import com.donaton.bff.dto.api.FrontendDonationDtos.CreateDonacionRequest;
import com.donaton.bff.dto.api.FrontendDonationDtos.DonacionResponse;
import com.donaton.bff.mapper.DonationMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DonationBffService {

	private final DonationServiceClient donationServiceClient;

	public DonationBffService(DonationServiceClient donationServiceClient) {
		this.donationServiceClient = donationServiceClient;
	}

	public List<DonacionResponse> list(String authorization) {
		List<DonacionResponse> donations = donationServiceClient.list(authorization).stream()
			.map(DonationMapper::toFrontend)
			.toList();
		log.info("BFF listó {} donaciones", donations.size());
		return donations;
	}

	public DonacionResponse getById(long id, String authorization) {
		log.debug("BFF consulta donación id={}", id);
		return DonationMapper.toFrontend(donationServiceClient.getById(id, authorization));
	}

	public DonacionResponse create(CreateDonacionRequest request, String authorization) {
		var created = donationServiceClient.create(
			DonationMapper.toServiceRequest(request),
			authorization
		);
		DonacionResponse response = DonationMapper.toFrontend(created);
		log.info("BFF creó donación id={}", response.idDonacion());
		return response;
	}

	public DonacionResponse update(long id, CreateDonacionRequest request, String authorization) {
		var updated = donationServiceClient.update(
			id,
			DonationMapper.toServiceRequest(request),
			authorization
		);
		log.info("BFF actualizó donación id={}", id);
		return DonationMapper.toFrontend(updated);
	}

	public void delete(long id, String authorization) {
		donationServiceClient.delete(id, authorization);
		log.info("BFF eliminó donación id={}", id);
	}
}
