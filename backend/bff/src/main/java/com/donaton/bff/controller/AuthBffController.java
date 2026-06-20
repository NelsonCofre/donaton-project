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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Auth (Frontend)", description = "Autenticación expuesta al navegador")
@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthBffController {

	private final AuthServiceClient authServiceClient;

	public AuthBffController(AuthServiceClient authServiceClient) {
		this.authServiceClient = authServiceClient;
	}

	@Operation(
		summary = "Iniciar sesión",
		description = "Valida credenciales contra auth-service y devuelve token JWT y datos del usuario para la UI."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Sesión iniciada",
			content = @Content(schema = @Schema(implementation = AuthResponse.class))),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "401", description = "Credenciales inválidas")
	})
	@PostMapping("/login")
	public AuthResponse login(@Valid @RequestBody LoginRequest request) {
		var auth = authServiceClient.login(AuthMapper.toAuthLogin(request));
		return AuthMapper.toFrontendAuth(auth);
	}

	@Operation(
		summary = "Registrar usuario",
		description = "Crea una cuenta nueva en auth-service y devuelve los datos del usuario registrado."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Usuario registrado",
			content = @Content(schema = @Schema(implementation = UserResponse.class))),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "409", description = "El correo ya está registrado")
	})
	@PostMapping("/register")
	public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
		var auth = authServiceClient.register(AuthMapper.toAuthRegister(request));
		return ResponseEntity.status(HttpStatus.CREATED).body(AuthMapper.toFrontendUser(auth.user()));
	}
}
