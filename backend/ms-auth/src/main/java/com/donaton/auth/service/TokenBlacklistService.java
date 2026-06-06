package com.donaton.auth.service;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.donaton.auth.model.BlacklistedToken;
import com.donaton.auth.repository.BlacklistedTokenRepository;

@Service
public class TokenBlacklistService {

	private final BlacklistedTokenRepository blacklistedTokenRepository;

	public TokenBlacklistService(BlacklistedTokenRepository blacklistedTokenRepository) {
		this.blacklistedTokenRepository = blacklistedTokenRepository;
	}

	@Transactional
	public void blacklist(String token, OffsetDateTime expiresAt) {
		if (blacklistedTokenRepository.existsByToken(token)) {
			return;
		}
		BlacklistedToken blacklistedToken = new BlacklistedToken();
		blacklistedToken.setToken(token);
		blacklistedToken.setExpiresAt(expiresAt);
		blacklistedTokenRepository.save(blacklistedToken);
	}

	public boolean isBlacklisted(String token) {
		blacklistedTokenRepository.deleteByExpiresAtBefore(OffsetDateTime.now());
		return blacklistedTokenRepository.existsByToken(token);
	}
}
