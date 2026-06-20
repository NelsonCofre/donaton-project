package com.donaton.auth.service;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.donaton.auth.exception.InvalidTokenException;
import com.donaton.auth.model.RefreshToken;
import com.donaton.auth.model.UserAccount;
import com.donaton.auth.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;
	private final long refreshTokenExpirationDays;

	public RefreshTokenService(
		RefreshTokenRepository refreshTokenRepository,
		@Value("${security.jwt.refresh-token-expiration-days:7}") long refreshTokenExpirationDays
	) {
		this.refreshTokenRepository = refreshTokenRepository;
		this.refreshTokenExpirationDays = refreshTokenExpirationDays;
	}

	@Transactional
	public RefreshToken createRefreshToken(UserAccount userAccount) {
		refreshTokenRepository.deleteByExpiresAtBefore(OffsetDateTime.now());
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(UUID.randomUUID().toString() + UUID.randomUUID());
		refreshToken.setUser(userAccount);
		refreshToken.setExpiresAt(OffsetDateTime.now().plusDays(refreshTokenExpirationDays));
		refreshToken.setRevoked(false);
		return refreshTokenRepository.save(refreshToken);
	}

	@Transactional(readOnly = true)
	public RefreshToken validateRefreshToken(String token) {
		RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
			.orElseThrow(() -> new InvalidTokenException("Refresh token not found"));

		if (refreshToken.isRevoked() || refreshToken.getExpiresAt().isBefore(OffsetDateTime.now())) {
			throw new InvalidTokenException("Refresh token is invalid or expired");
		}
		return refreshToken;
	}

	@Transactional
	public void revokeToken(String token) {
		refreshTokenRepository.findByToken(token).ifPresent(refreshToken -> {
			refreshToken.setRevoked(true);
			refreshTokenRepository.save(refreshToken);
		});
	}

	@Transactional
	public void revokeAllUserTokens(UserAccount userAccount) {
		refreshTokenRepository.findByUserAndRevokedFalse(userAccount).forEach(refreshToken -> {
			refreshToken.setRevoked(true);
			refreshTokenRepository.save(refreshToken);
		});
	}
}
