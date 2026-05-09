package com.donaton.auth.service;

import com.donaton.auth.dto.AuthResponse;
import com.donaton.auth.dto.LoginRequest;
import com.donaton.auth.dto.RegisterRequest;
import com.donaton.auth.dto.UserResponse;
import com.donaton.auth.exception.ApiException;
import com.donaton.auth.model.User;
import com.donaton.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	@Transactional
	public AuthResponse register(RegisterRequest request) {
		if (userRepository.existsByEmailIgnoreCase(request.email())) {
			throw new ApiException(HttpStatus.CONFLICT, "El correo ya está registrado");
		}
		String hash = passwordEncoder.encode(request.password());
		User user = new User(request.email(), hash, request.name().trim());
		userRepository.save(user);
		String token = jwtService.generateToken(user);
		return new AuthResponse(UserResponse.from(user), token);
	}

	@Transactional(readOnly = true)
	public AuthResponse login(LoginRequest request) {
		User user = userRepository.findByEmailIgnoreCase(request.email())
				.orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas"));
		if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
			throw new ApiException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
		}
		String token = jwtService.generateToken(user);
		return new AuthResponse(UserResponse.from(user), token);
	}

}
