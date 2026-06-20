package com.donaton.auth.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.donaton.auth.exception.InvalidTokenException;
import com.donaton.auth.model.Role;
import com.donaton.auth.model.UserAccount;

class JwtServiceTest {

	private static final String SECRET = "test-secret-key-with-at-least-32-characters!!";

	private JwtService jwtService;
	private UserAccount user;

	@BeforeEach
	void setUp() {
		jwtService = new JwtService(SECRET, 15);
		user = new UserAccount();
		user.setEmail("usuario@donaton.cl");
		user.setRole(Role.USER);
		user.setEnabled(true);
	}

	@Test
	void generateAccessTokenContainsSubjectAndClaims() {
		String token = jwtService.generateAccessToken(user);

		assertThat(token).isNotBlank();
		assertThat(jwtService.extractSubject(token)).isEqualTo("usuario@donaton.cl");
		assertThat(jwtService.isAccessToken(token)).isTrue();
		assertThat(jwtService.extractPermissions(token)).contains("USER_READ");
	}

	@Test
	void extractExpirationIsInTheFuture() {
		String token = jwtService.generateAccessToken(user);

		assertThat(jwtService.extractExpiration(token)).isAfter(java.time.OffsetDateTime.now());
	}

	@Test
	void getAccessTokenExpirationSecondsMatchesConfiguration() {
		assertThat(jwtService.getAccessTokenExpirationSeconds()).isEqualTo(15 * 60);
	}

	@Test
	void extractClaimsRejectsTamperedToken() {
		String token = jwtService.generateAccessToken(user);
		String tampered = token.substring(0, token.length() - 2) + "xx";

		assertThatThrownBy(() -> jwtService.extractClaims(tampered))
			.isInstanceOf(InvalidTokenException.class)
			.hasMessageContaining("invalid");
	}

	@Test
	void adminTokenIncludesAdminPermissions() {
		user.setRole(Role.ADMIN);
		String token = jwtService.generateAccessToken(user);

		Set<String> permissions = jwtService.extractPermissions(token);

		assertThat(permissions).contains("USER_READ", "USER_WRITE", "AUTH_MANAGE");
	}
}
