package com.donaton.bff.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.donaton.bff.client.NecessityServiceClient;
import com.donaton.bff.dto.api.FrontendNecessityDtos.CreateNecesidadRequest;
import com.donaton.bff.dto.api.FrontendNecessityDtos.NecesidadResponse;
import com.donaton.bff.mapper.NecessityMapper;

@Service
public class NecessityBffService {

	private final NecessityServiceClient necessityServiceClient;

	public NecessityBffService(NecessityServiceClient necessityServiceClient) {
		this.necessityServiceClient = necessityServiceClient;
	}

	public List<NecesidadResponse> list() {
		return necessityServiceClient.list().stream()
			.map(NecessityMapper::toFrontend)
			.toList();
	}

	public NecesidadResponse getById(long id) {
		return NecessityMapper.toFrontend(necessityServiceClient.getById(id));
	}

	public NecesidadResponse create(CreateNecesidadRequest request) {
		var created = necessityServiceClient.create(NecessityMapper.toServiceRequest(request));
		return NecessityMapper.toFrontend(created);
	}

	public NecesidadResponse update(long id, CreateNecesidadRequest request) {
		var updated = necessityServiceClient.update(id, NecessityMapper.toServiceRequest(request));
		return NecessityMapper.toFrontend(updated);
	}

	public void delete(long id) {
		necessityServiceClient.delete(id);
	}
}
