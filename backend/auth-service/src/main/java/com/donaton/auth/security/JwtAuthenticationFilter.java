package com.donaton.auth.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.donaton.auth.exception.InvalidTokenException;
import com.donaton.auth.service.TokenBlacklistService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final CustomUserDetailsService customUserDetailsService;
	private final TokenBlacklistService tokenBlacklistService;

	public JwtAuthenticationFilter(
		JwtService jwtService,
		CustomUserDetailsService customUserDetailsService,
		TokenBlacklistService tokenBlacklistService
	) {
		this.jwtService = jwtService;
		this.customUserDetailsService = customUserDetailsService;
		this.tokenBlacklistService = tokenBlacklistService;
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authorizationHeader.substring(7).trim();
		try {
			if (tokenBlacklistService.isBlacklisted(token) || !jwtService.isAccessToken(token)) {
				throw new InvalidTokenException("Token is not valid for authentication");
			}

			String username = jwtService.extractSubject(token);
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					userDetails,
					null,
					userDetails.getAuthorities()
				);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (InvalidTokenException exception) {
			SecurityContextHolder.clearContext();
		}

		filterChain.doFilter(request, response);
	}
}
