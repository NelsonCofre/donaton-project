package com.donaton.auth.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.donaton.auth.model.RefreshToken;
import com.donaton.auth.model.UserAccount;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByToken(String token);

	List<RefreshToken> findByUserAndRevokedFalse(UserAccount user);

	void deleteByExpiresAtBefore(OffsetDateTime threshold);
}
