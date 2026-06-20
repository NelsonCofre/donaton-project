package com.donaton.bff.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.donaton.bff.client.AuthServiceClient;
import com.donaton.bff.exception.UnauthorizedException;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

	@Mock
	private AuthServiceClient authServiceClient;

	private JwtAuthFilter filter;

	@BeforeEach
	void setUp() {
		filter = new JwtAuthFilter(authServiceClient);
	}

	@Test
	void optionsRequestSkipsTokenValidation() throws Exception {
		var request = new MockHttpServletRequest("OPTIONS", "/api/donations");
		var response = new MockHttpServletResponse();
		var chain = new MockFilterChain();

		filter.doFilter(request, response, chain);

		assertThat(chain.getRequest()).isNotNull();
		verify(authServiceClient, never()).validateToken(anyString());
	}

	@Test
	void shouldNotFilterSkipsPublicPaths() throws Exception {
		var request = new MockHttpServletRequest("GET", "/api/auth/login");
		var response = new MockHttpServletResponse();
		var chain = new MockFilterChain();

		filter.doFilter(request, response, chain);

		assertThat(chain.getRequest()).isNotNull();
		verify(authServiceClient, never()).validateToken(anyString());
	}

	@Test
	void missingAuthorizationHeaderReturns401() throws Exception {
		var request = new MockHttpServletRequest("GET", "/api/donations");
		var response = new MockHttpServletResponse();
		var chain = new MockFilterChain();

		filter.doFilter(request, response, chain);

		assertThat(response.getStatus()).isEqualTo(401);
		assertThat(response.getContentAsString()).contains("Missing or invalid Authorization header");
		assertThat(chain.getRequest()).isNull();
	}

	@Test
	void validBearerTokenContinuesFilterChain() throws Exception {
		var request = new MockHttpServletRequest("GET", "/api/v1/necessities");
		request.addHeader("Authorization", "Bearer valid-token");
		var response = new MockHttpServletResponse();
		var chain = new MockFilterChain();

		doNothing().when(authServiceClient).validateToken("valid-token");

		filter.doFilter(request, response, chain);

		assertThat(chain.getRequest()).isNotNull();
		verify(authServiceClient).validateToken("valid-token");
	}

	@Test
	void invalidTokenReturns401() throws Exception {
		var request = new MockHttpServletRequest("POST", "/api/v1/logistics/shipments");
		request.addHeader("Authorization", "Bearer bad-token");
		var response = new MockHttpServletResponse();
		var chain = new MockFilterChain();

		doThrow(new UnauthorizedException("invalid"))
			.when(authServiceClient).validateToken("bad-token");

		filter.doFilter(request, response, chain);

		assertThat(response.getStatus()).isEqualTo(401);
		assertThat(response.getContentAsString()).contains("Invalid or expired token");
		assertThat(chain.getRequest()).isNull();
	}
}
