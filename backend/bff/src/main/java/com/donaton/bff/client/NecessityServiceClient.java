package com.donaton.bff.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.donaton.bff.dto.necessity.NecessityServiceDtos.NecessityRequestDto;
import com.donaton.bff.dto.necessity.NecessityServiceDtos.NecessityResponseDto;

@Component
public class NecessityServiceClient {

	private final RestClient restClient;

	public NecessityServiceClient(@Qualifier("necessityRestClient") RestClient restClient) {
		this.restClient = restClient;
	}

	public List<NecessityResponseDto> list() {
		NecessityResponseDto[] body = RestClientHelper.withErrorHandling(
			restClient.get().uri("/api/v1/necessities").retrieve()
		).body(NecessityResponseDto[].class);
		if (body == null) {
			return List.of();
		}
		return Arrays.asList(body);
	}

	public NecessityResponseDto getById(long id) {
		return RestClientHelper.withErrorHandling(
			restClient.get()
				.uri("/api/v1/necessities/{id}", id)
				.retrieve()
		).body(NecessityResponseDto.class);
	}

	public NecessityResponseDto create(NecessityRequestDto request) {
		return RestClientHelper.withErrorHandling(
			restClient.post()
				.uri("/api/v1/necessities")
				.body(request)
				.retrieve()
		).body(NecessityResponseDto.class);
	}

	public NecessityResponseDto update(long id, NecessityRequestDto request) {
		return RestClientHelper.withErrorHandling(
			restClient.put()
				.uri("/api/v1/necessities/{id}", id)
				.body(request)
				.retrieve()
		).body(NecessityResponseDto.class);
	}

	public void delete(long id) {
		RestClientHelper.withErrorHandling(
			restClient.delete()
				.uri("/api/v1/necessities/{id}", id)
				.retrieve()
		).toBodilessEntity();
	}
}
