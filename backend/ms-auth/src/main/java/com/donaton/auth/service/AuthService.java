package com.donaton.auth.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.donaton.auth.dto.AuthResponse;
import com.donaton.auth.dto.LoginRequest;
import com.donaton.auth.dto.LogoutRequest;
import com.donaton.auth.dto.LogoutResponse;
import com.donaton.auth.dto.RefreshTokenRequest;
import com.donaton.auth.dto.RegisterRequest;
import com.donaton.auth.dto.TokenValidationRequest;
import com.donaton.auth.dto.TokenValidationResponse;
import com.donaton.auth.dto.UserResponse;
import com.donaton.auth.exception.AuthenticationFailedException;
import com.donaton.auth.exception.ConflictException;
import com.donaton.auth.exception.InvalidTokenException;
import com.donaton.auth.exception.ResourceNotFoundException;
import com.donaton.auth.model.RefreshToken;
import com.donaton.auth.model.Role;
import com.donaton.auth.model.UserAccount;
import com.donaton.auth.repository.UserAccountRepository;
import com.donaton.auth.security.JwtService;

@Service
public class AuthService {

	private final UserAccountRepository userAccountRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final RefreshTokenService refreshTokenService;
	private final TokenBlacklistService tokenBlacklistService;

	public AuthService(
		UserAccountRepository userAccountRepository,
		PasswordEncoder passwordEncoder,
		AuthenticationManager authenticationManager,
		JwtService jwtService,
		RefreshTokenService refreshTokenService,
		TokenBlacklistService tokenBlacklistService
	) {
		this.userAccountRepository = userAccountRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.refreshTokenService = refreshTokenService;
		this.tokenBlacklistService = tokenBlacklistService;
	}

	@Transactional
	public AuthResponse register(RegisterRequest request) {
		if (userAccountRepository.existsByEmailIgnoreCase(request.email())) {
			throw new ConflictException("A user with this email already exists");
		}

		UserAccount userAccount = new UserAccount();
		userAccount.setEmail(request.email().trim().toLowerCase());
		userAccount.setPasswordHash(passwordEncoder.encode(request.password()));
		userAccount.setRole(resolveRole(request.role()));
		userAccount.setEnabled(true);

		UserAccount savedUser = userAccountRepository.save(userAccount);
		return buildAuthResponse(savedUser);
	}

	@Transactional(readOnly = true)
	public void validateCredentials(LoginRequest request) {
		UserAccount user = userAccountRepository.findByEmailIgnoreCase(request.email())
			.orElseThrow(() -> new AuthenticationFailedException("Invalid credentials"));

		if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
			throw new AuthenticationFailedException("Invalid credentials");
		}
	}

	@Transactional
	public AuthResponse login(LoginRequest request) {
		try {
			authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.email(), request.password())
			);
		} catch (BadCredentialsException exception) {
			throw new AuthenticationFailedException("Invalid credentials");
		}

		UserAccount userAccount = userAccountRepository.findByEmailIgnoreCase(request.email())
			.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		return buildAuthResponse(userAccount);
	}

	@Transactional(readOnly = true)
	public TokenValidationResponse validateToken(TokenValidationRequest request) {
		String token = request.token().trim();
		if (tokenBlacklistService.isBlacklisted(token)) {
			throw new InvalidTokenException("Token is blacklisted");
		}
		if (!jwtService.isAccessToken(token)) {
			throw new InvalidTokenException("Token type is not valid for access");
		}

		String subject = jwtService.extractSubject(token);
		UserAccount userAccount = userAccountRepository.findByEmailIgnoreCase(subject)
			.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		return new TokenValidationResponse(
			true,
			subject,
			userAccount.getRole(),
			extractPermissionNames(userAccount)
		);
	}

	@Transactional
	public AuthResponse refreshToken(RefreshTokenRequest request) {
		RefreshToken refreshToken = refreshTokenService.validateRefreshToken(request.refreshToken());
		UserAccount userAccount = refreshToken.getUser();
		refreshToken.setRevoked(true);

		return buildAuthResponse(userAccount);
	}

	@Transactional
	public LogoutResponse logout(String bearerToken, LogoutRequest request) {
		String accessToken = extractTokenFromHeader(bearerToken);
		if (accessToken != null) {
			if (!jwtService.isAccessToken(accessToken)) {
				throw new InvalidTokenException("Token type is not valid for logout");
			}
			String email = jwtService.extractSubject(accessToken);
			UserAccount userAccount = userAccountRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

			tokenBlacklistService.blacklist(accessToken, jwtService.extractExpiration(accessToken));
			refreshTokenService.revokeAllUserTokens(userAccount);
		}

		if (request != null && request.refreshToken() != null && !request.refreshToken().isBlank()) {
			refreshTokenService.revokeToken(request.refreshToken().trim());
		}

		return new LogoutResponse("Session closed successfully");
	}

	@Transactional(readOnly = true)
	public UserResponse getCurrentUserProfile(String email) {
		UserAccount userAccount = userAccountRepository.findByEmailIgnoreCase(email)
			.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		return mapUserResponse(userAccount);
	}

	private AuthResponse buildAuthResponse(UserAccount userAccount) {
		String accessToken = jwtService.generateAccessToken(userAccount);
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userAccount);

		return new AuthResponse(
			accessToken,
			refreshToken.getToken(),
			"Bearer",
			jwtService.getAccessTokenExpirationSeconds(),
			mapUserResponse(userAccount)
		);
	}

	private UserResponse mapUserResponse(UserAccount userAccount) {
		return new UserResponse(
			userAccount.getId(),
			userAccount.getEmail(),
			userAccount.getRole(),
			extractPermissionNames(userAccount)
		);
	}

	private Set<String> extractPermissionNames(UserAccount userAccount) {
		return userAccount.getRole().getPermissions().stream()
			.map(Enum::name)
			.collect(Collectors.toSet());
	}

	private String extractTokenFromHeader(String bearerToken) {
		if (bearerToken == null || bearerToken.isBlank() || !bearerToken.startsWith("Bearer ")) {
			return null;
		}
		return bearerToken.substring(7).trim();
	}

	private Role resolveRole(Role requestedRole) {
		return requestedRole == null ? Role.USER : requestedRole;
	}
}
