package com.donaton.auth.model;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "blacklisted_tokens")
public class BlacklistedToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 1000)
	private String token;

	@Column(nullable = false)
	private OffsetDateTime expiresAt;

	@Column(nullable = false, updatable = false)
	private OffsetDateTime createdAt;

	@PrePersist
	public void onCreate() {
		createdAt = OffsetDateTime.now();
	}
}
