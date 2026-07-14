package com.donaton.bff.service;

import org.springframework.stereotype.Service;

import com.donaton.bff.client.AuthServiceClient;
import com.donaton.bff.dto.api.FrontendAuthDtos.AuthResponse;
import com.donaton.bff.dto.api.FrontendAuthDtos.LoginRequest;
import com.donaton.bff.dto.api.FrontendAuthDtos.RegisterRequest;
import com.donaton.bff.dto.api.FrontendAuthDtos.UserResponse;
import com.donaton.bff.mapper.AuthMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthBffService {

	private final AuthServiceClient authServiceClient;

	public AuthBffService(AuthServiceClient authServiceClient) {
		this.authServiceClient = authServiceClient;
	}

	public AuthResponse login(LoginRequest request) {
		log.info("BFF login solicitado email={}", request.email());
		var auth = authServiceClient.login(AuthMapper.toAuthLogin(request));
		log.info("BFF login completado email={}", request.email());
		return AuthMapper.toFrontendAuth(auth);
	}

	public UserResponse register(RegisterRequest request) {
		log.info("BFF registro solicitado email={}", request.email());
		var auth = authServiceClient.register(AuthMapper.toAuthRegister(request));
		UserResponse user = AuthMapper.toFrontendUser(auth.user());
		log.info("BFF registro completado email={}", user.email());
		return user;
	}
}
