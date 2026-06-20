package com.donaton.bff.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.donaton.bff.dto.donation.DonationServiceDtos.DonationRequestDto;
import com.donaton.bff.dto.donation.DonationServiceDtos.DonationResponseDto;

@Component
public class DonationServiceClient {

	private final RestClient restClient;

	public DonationServiceClient(@Qualifier("donationRestClient") RestClient restClient) {
		this.restClient = restClient;
	}

	public List<DonationResponseDto> list(String authorizationHeader) {
		DonationResponseDto[] body = RestClientHelper.withErrorHandling(
			withAuth(restClient.get(), authorizationHeader).uri("/api/v1/donations").retrieve()
		).body(DonationResponseDto[].class);
		if (body == null) {
			return List.of();
		}
		return Arrays.asList(body);
	}

	public DonationResponseDto getById(long id, String authorizationHeader) {
		return RestClientHelper.withErrorHandling(
			withAuth(restClient.get(), authorizationHeader)
				.uri("/api/v1/donations/{id}", id)
				.retrieve()
		).body(DonationResponseDto.class);
	}

	public DonationResponseDto create(DonationRequestDto request, String authorizationHeader) {
		return RestClientHelper.withErrorHandling(
			withAuth(restClient.post(), authorizationHeader)
				.uri("/api/v1/donations")
				.body(request)
				.retrieve()
		).body(DonationResponseDto.class);
	}

	public DonationResponseDto update(
		long id,
		DonationRequestDto request,
		String authorizationHeader
	) {
		return RestClientHelper.withErrorHandling(
			withAuth(restClient.put(), authorizationHeader)
				.uri("/api/v1/donations/{id}", id)
				.body(request)
				.retrieve()
		).body(DonationResponseDto.class);
	}

	public void delete(long id, String authorizationHeader) {
		RestClientHelper.withErrorHandling(
			withAuth(restClient.delete(), authorizationHeader)
				.uri("/api/v1/donations/{id}", id)
				.retrieve()
		).toBodilessEntity();
	}

	private static RestClient.RequestHeadersUriSpec<?> withAuth(
		RestClient.RequestHeadersUriSpec<?> spec,
		String authorizationHeader
	) {
		if (authorizationHeader != null && !authorizationHeader.isBlank()) {
			spec.header(HttpHeaders.AUTHORIZATION, authorizationHeader);
		}
		return spec;
	}

	private static RestClient.RequestBodyUriSpec withAuth(
		RestClient.RequestBodyUriSpec spec,
		String authorizationHeader
	) {
		if (authorizationHeader != null && !authorizationHeader.isBlank()) {
			spec.header(HttpHeaders.AUTHORIZATION, authorizationHeader);
		}
		return spec;
	}
}
