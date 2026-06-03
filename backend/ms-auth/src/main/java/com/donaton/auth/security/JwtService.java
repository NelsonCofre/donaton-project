package com.donaton.auth.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.donaton.auth.exception.InvalidTokenException;
import com.donaton.auth.model.UserAccount;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final SecretKey signingKey;
	private final long accessTokenExpirationMinutes;

	public JwtService(
		@Value("${security.jwt.secret:default-local-auth-secret-minimum-32-characters-long!!}") String secret,
		@Value("${security.jwt.access-token-expiration-minutes:15}") long accessTokenExpirationMinutes
	) {
		this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.accessTokenExpirationMinutes = accessTokenExpirationMinutes;
	}

	public String generateAccessToken(UserAccount userAccount) {
		Instant now = Instant.now();
		Instant expiration = now.plusSeconds(accessTokenExpirationMinutes * 60);

		Map<String, Object> claims = Map.of(
			"role", userAccount.getRole().name(),
			"permissions", userAccount.getRole().getPermissions().stream().map(Enum::name).toList(),
			"tokenType", "access"
		);

		return Jwts.builder()
			.subject(userAccount.getEmail())
			.claims(claims)
			.issuedAt(Date.from(now))
			.expiration(Date.from(expiration))
			.signWith(signingKey)
			.compact();
	}

	public Claims extractClaims(String token) {
		try {
			return Jwts.parser()
				.verifyWith(signingKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		} catch (JwtException | IllegalArgumentException exception) {
			throw new InvalidTokenException("Token is invalid or expired");
		}
	}

	public String extractSubject(String token) {
		return extractClaims(token).getSubject();
	}

	public OffsetDateTime extractExpiration(String token) {
		Date expiration = extractClaims(token).getExpiration();
		return expiration.toInstant().atOffset(ZoneOffset.UTC);
	}

	public boolean isAccessToken(String token) {
		return "access".equals(extractClaims(token).get("tokenType"));
	}

	@SuppressWarnings("unchecked")
	public Set<String> extractPermissions(String token) {
		return Set.copyOf((java.util.List<String>) extractClaims(token).get("permissions"));
	}

	public long getAccessTokenExpirationSeconds() {
		return accessTokenExpirationMinutes * 60;
	}
}
