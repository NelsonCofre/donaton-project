package com.donaton.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.donaton.auth.dto.AuthResponse;
import com.donaton.auth.dto.LoginRequest;
import com.donaton.auth.dto.LogoutRequest;
import com.donaton.auth.dto.LogoutResponse;
import com.donaton.auth.dto.RefreshTokenRequest;
import com.donaton.auth.dto.RegisterRequest;
import com.donaton.auth.dto.TokenValidationRequest;
import com.donaton.auth.dto.TokenValidationResponse;
import com.donaton.auth.dto.UserResponse;
import com.donaton.auth.service.AuthService;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
		return ResponseEntity.ok(authService.login(request));
	}

	@PostMapping("/validate-credentials")
	public ResponseEntity<Void> validateCredentials(@Valid @RequestBody LoginRequest request) {
		authService.validateCredentials(request);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/validate-token")
	public ResponseEntity<TokenValidationResponse> validateToken(@Valid @RequestBody TokenValidationRequest request) {
		return ResponseEntity.ok(authService.validateToken(request));
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
		return ResponseEntity.ok(authService.refreshToken(request));
	}

	@PostMapping("/logout")
	public ResponseEntity<LogoutResponse> logout(
		@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
		@RequestBody(required = false) LogoutRequest request
	) {
		return ResponseEntity.ok(authService.logout(authorizationHeader, request));
	}

	@GetMapping("/me")
	public ResponseEntity<UserResponse> me(Authentication authentication) {
		return ResponseEntity.ok(authService.getCurrentUserProfile(authentication.getName()));
	}
}
