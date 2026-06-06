package com.donaton.bff.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.donaton.bff.client.AuthServiceClient;
import com.donaton.bff.dto.api.FrontendAuthDtos.AuthResponse;
import com.donaton.bff.dto.api.FrontendAuthDtos.LoginRequest;
import com.donaton.bff.dto.api.FrontendAuthDtos.RegisterRequest;
import com.donaton.bff.dto.api.FrontendAuthDtos.UserResponse;
import com.donaton.bff.mapper.AuthMapper;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthBffController {

	private final AuthServiceClient authServiceClient;

	public AuthBffController(AuthServiceClient authServiceClient) {
		this.authServiceClient = authServiceClient;
	}

	@PostMapping("/login")
	public AuthResponse login(@Valid @RequestBody LoginRequest request) {
		var auth = authServiceClient.login(AuthMapper.toAuthLogin(request));
		return AuthMapper.toFrontendAuth(auth);
	}

	@PostMapping("/register")
	public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
		var auth = authServiceClient.register(AuthMapper.toAuthRegister(request));
		return ResponseEntity.status(HttpStatus.CREATED).body(AuthMapper.toFrontendUser(auth.user()));
	}
}
