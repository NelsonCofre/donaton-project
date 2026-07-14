package com.donaton.bff.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.donaton.bff.client.NecessityServiceClient;
import com.donaton.bff.dto.api.FrontendNecessityDtos.CreateNecesidadRequest;
import com.donaton.bff.dto.api.FrontendNecessityDtos.NecesidadResponse;
import com.donaton.bff.mapper.NecessityMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NecessityBffService {

	private final NecessityServiceClient necessityServiceClient;

	public NecessityBffService(NecessityServiceClient necessityServiceClient) {
		this.necessityServiceClient = necessityServiceClient;
	}

	public List<NecesidadResponse> list() {
		List<NecesidadResponse> necessities = necessityServiceClient.list().stream()
			.map(NecessityMapper::toFrontend)
			.toList();
		log.info("BFF listó {} necesidades", necessities.size());
		return necessities;
	}

	public NecesidadResponse getById(long id) {
		log.debug("BFF consulta necesidad id={}", id);
		return NecessityMapper.toFrontend(necessityServiceClient.getById(id));
	}

	public NecesidadResponse create(CreateNecesidadRequest request) {
		var created = necessityServiceClient.create(NecessityMapper.toServiceRequest(request));
		NecesidadResponse response = NecessityMapper.toFrontend(created);
		log.info("BFF creó necesidad id={}", response.idNecesidad());
		return response;
	}

	public NecesidadResponse update(long id, CreateNecesidadRequest request) {
		var updated = necessityServiceClient.update(id, NecessityMapper.toServiceRequest(request));
		log.info("BFF actualizó necesidad id={}", id);
		return NecessityMapper.toFrontend(updated);
	}

	public void delete(long id) {
		necessityServiceClient.delete(id);
		log.info("BFF eliminó necesidad id={}", id);
	}
}
