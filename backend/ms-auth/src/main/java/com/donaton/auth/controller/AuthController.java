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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Autenticación", description = "Registro, sesión JWT, validación de tokens y perfil de usuario")
@Validated
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@Operation(
		summary = "Registrar usuario",
		description = "Crea una cuenta nueva y devuelve access token, refresh token y datos del usuario.",
		tags = { "Registro" }
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Usuario registrado",
			content = @Content(schema = @Schema(implementation = AuthResponse.class))),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "409", description = "El correo ya está registrado")
	})
	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
	}

	@Operation(
		summary = "Iniciar sesión",
		description = "Valida credenciales y devuelve tokens JWT para consumir endpoints protegidos.",
		tags = { "Sesión" }
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Sesión iniciada",
			content = @Content(schema = @Schema(implementation = AuthResponse.class))),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "401", description = "Credenciales inválidas")
	})
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
		return ResponseEntity.ok(authService.login(request));
	}

	@Operation(
		summary = "Validar credenciales",
		description = "Comprueba email y contraseña sin emitir tokens. Responde 204 si son válidas.",
		tags = { "Validación" }
	)
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Credenciales válidas"),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "401", description = "Credenciales inválidas")
	})
	@PostMapping("/validate-credentials")
	public ResponseEntity<Void> validateCredentials(@Valid @RequestBody LoginRequest request) {
		authService.validateCredentials(request);
		return ResponseEntity.noContent().build();
	}

	@Operation(
		summary = "Validar token",
		description = "Verifica que un access token JWT sea válido y no esté revocado. Usado por el BFF y otros servicios.",
		tags = { "Validación" }
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Resultado de la validación",
			content = @Content(schema = @Schema(implementation = TokenValidationResponse.class))),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "401", description = "Token inválido o expirado")
	})
	@PostMapping("/validate-token")
	public ResponseEntity<TokenValidationResponse> validateToken(@Valid @RequestBody TokenValidationRequest request) {
		return ResponseEntity.ok(authService.validateToken(request));
	}

	@Operation(
		summary = "Refrescar sesión",
		description = "Emite un nuevo par de tokens usando un refresh token válido.",
		tags = { "Sesión" }
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Tokens renovados",
			content = @Content(schema = @Schema(implementation = AuthResponse.class))),
		@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		@ApiResponse(responseCode = "401", description = "Refresh token inválido o expirado")
	})
	@PostMapping("/refresh-token")
	public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
		return ResponseEntity.ok(authService.refreshToken(request));
	}

	@Operation(
		summary = "Cerrar sesión",
		description = "Revoca refresh tokens y agrega el access token actual a la blacklist.",
		tags = { "Sesión" },
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Sesión cerrada",
			content = @Content(schema = @Schema(implementation = LogoutResponse.class))),
		@ApiResponse(responseCode = "401", description = "Token ausente o inválido")
	})
	@PostMapping("/logout")
	public ResponseEntity<LogoutResponse> logout(
		@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
		@RequestBody(required = false) LogoutRequest request
	) {
		return ResponseEntity.ok(authService.logout(authorizationHeader, request));
	}

	@Operation(
		summary = "Obtener perfil autenticado",
		description = "Devuelve el usuario asociado al JWT enviado en el header Authorization.",
		tags = { "Perfil" },
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Perfil del usuario",
			content = @Content(schema = @Schema(implementation = UserResponse.class))),
		@ApiResponse(responseCode = "401", description = "Token ausente o inválido")
	})
	@GetMapping("/me")
	public ResponseEntity<UserResponse> me(Authentication authentication) {
		return ResponseEntity.ok(authService.getCurrentUserProfile(authentication.getName()));
	}
}
