package com.donaton.bff.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.donaton.bff.client.AuthServiceClient;
import com.donaton.bff.exception.UnauthorizedException;
import com.donaton.bff.exception.UpstreamServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private final AuthServiceClient authServiceClient;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public JwtAuthFilter(AuthServiceClient authServiceClient) {
		this.authServiceClient = authServiceClient;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			return true;
		}
		String path = request.getRequestURI();
		return !path.startsWith("/api/donations");
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		String authorization = request.getHeader("Authorization");
		if (authorization == null || !authorization.startsWith("Bearer ")) {
			writeUnauthorized(response, "Missing or invalid Authorization header");
			return;
		}
		String token = authorization.substring(7).trim();
		if (token.isEmpty()) {
			writeUnauthorized(response, "Missing or invalid Authorization header");
			return;
		}
		try {
			authServiceClient.validateToken(token);
			filterChain.doFilter(request, response);
		} catch (UnauthorizedException | UpstreamServiceException ex) {
			writeUnauthorized(response, "Invalid or expired token");
		}
	}

	private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		var body = java.util.Map.of(
			"status", 401,
			"message", message
		);
		response.getWriter().write(objectMapper.writeValueAsString(body));
	}
}
