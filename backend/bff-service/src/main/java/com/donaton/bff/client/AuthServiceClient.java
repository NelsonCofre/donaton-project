package com.donaton.bff.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.donaton.bff.dto.auth.AuthServiceDtos.AuthResponse;
import com.donaton.bff.dto.auth.AuthServiceDtos.LoginRequest;
import com.donaton.bff.dto.auth.AuthServiceDtos.RegisterRequest;
import com.donaton.bff.dto.auth.AuthServiceDtos.TokenValidationRequest;
import com.donaton.bff.dto.auth.AuthServiceDtos.TokenValidationResponse;
import com.donaton.bff.exception.UnauthorizedException;

@Component
public class AuthServiceClient {

	private final RestClient restClient;

	public AuthServiceClient(@Qualifier("authRestClient") RestClient restClient) {
		this.restClient = restClient;
	}

	public AuthResponse login(LoginRequest request) {
		return RestClientHelper.withErrorHandling(
			restClient.post().uri("/api/v1/auth/login").body(request).retrieve()
		).body(AuthResponse.class);
	}

	public AuthResponse register(RegisterRequest request) {
		return RestClientHelper.withErrorHandling(
			restClient.post().uri("/api/v1/auth/register").body(request).retrieve()
		).body(AuthResponse.class);
	}

	public void validateToken(String bearerToken) {
		TokenValidationResponse response = RestClientHelper.withErrorHandling(
			restClient.post()
				.uri("/api/v1/auth/validate-token")
				.body(new TokenValidationRequest(bearerToken))
				.retrieve()
		).body(TokenValidationResponse.class);

		if (response == null || !response.valid()) {
			throw new UnauthorizedException("Invalid or expired token");
		}
	}
}
