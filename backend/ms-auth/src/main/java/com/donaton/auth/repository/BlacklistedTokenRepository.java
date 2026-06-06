package com.donaton.auth.repository;

import java.time.OffsetDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.donaton.auth.model.BlacklistedToken;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {

	boolean existsByToken(String token);

	void deleteByExpiresAtBefore(OffsetDateTime threshold);
}
