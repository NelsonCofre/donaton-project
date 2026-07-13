package com.donaton.bff.service;

import org.springframework.stereotype.Service;

import com.donaton.bff.client.AuthServiceClient;
import com.donaton.bff.dto.api.FrontendAuthDtos.AuthResponse;
import com.donaton.bff.dto.api.FrontendAuthDtos.LoginRequest;
import com.donaton.bff.dto.api.FrontendAuthDtos.RegisterRequest;
import com.donaton.bff.dto.api.FrontendAuthDtos.UserResponse;
import com.donaton.bff.mapper.AuthMapper;

@Service
public class AuthBffService {

	private final AuthServiceClient authServiceClient;

	public AuthBffService(AuthServiceClient authServiceClient) {
		this.authServiceClient = authServiceClient;
	}

	public AuthResponse login(LoginRequest request) {
		var auth = authServiceClient.login(AuthMapper.toAuthLogin(request));
		return AuthMapper.toFrontendAuth(auth);
	}

	public UserResponse register(RegisterRequest request) {
		var auth = authServiceClient.register(AuthMapper.toAuthRegister(request));
		return AuthMapper.toFrontendUser(auth.user());
	}
}
