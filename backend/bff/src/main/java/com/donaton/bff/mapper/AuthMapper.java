package com.donaton.bff.mapper;

import com.donaton.bff.dto.api.FrontendAuthDtos.AuthResponse;
import com.donaton.bff.dto.api.FrontendAuthDtos.RegisterRequest;
import com.donaton.bff.dto.api.FrontendAuthDtos.UserResponse;
import com.donaton.bff.dto.auth.AuthServiceDtos;

public final class AuthMapper {

	private AuthMapper() {
	}

	public static AuthServiceDtos.RegisterRequest toAuthRegister(RegisterRequest request) {
		String role = request.rol() != null && !request.rol().isBlank()
			? request.rol().trim().toUpperCase()
			: "USER";
		return new AuthServiceDtos.RegisterRequest(request.email(), request.password(), role);
	}

	public static AuthServiceDtos.LoginRequest toAuthLogin(
		com.donaton.bff.dto.api.FrontendAuthDtos.LoginRequest request
	) {
		return new AuthServiceDtos.LoginRequest(request.email(), request.password());
	}

	public static AuthResponse toFrontendAuth(AuthServiceDtos.AuthResponse auth) {
		return new AuthResponse(auth.accessToken(), toFrontendUser(auth.user()));
	}

	public static UserResponse toFrontendUser(AuthServiceDtos.UserResponse user) {
		return new UserResponse(user.id(), user.email(), user.role());
	}
}
