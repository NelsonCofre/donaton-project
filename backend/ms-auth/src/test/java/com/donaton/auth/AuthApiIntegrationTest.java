package com.donaton.auth;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthApiIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void registerLoginAndValidateTokenFlow() throws Exception {
		String email = "test.user." + System.nanoTime() + "@donaton.cl";
		String password = "ClaveSegura1";

		String registerBody = objectMapper.writeValueAsString(Map.of(
			"email", email,
			"password", password,
			"role", "USER"
		));

		String registered = mockMvc.perform(post("/api/v1/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(registerBody))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.accessToken").value(notNullValue()))
			.andExpect(jsonPath("$.user.email").value(email))
			.andReturn()
			.getResponse()
			.getContentAsString();

		String accessToken = objectMapper.readTree(registered).get("accessToken").asText();

		String loginBody = objectMapper.writeValueAsString(Map.of("email", email, "password", password));
		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.accessToken").value(notNullValue()));

		String validateBody = objectMapper.writeValueAsString(Map.of("token", accessToken));
		mockMvc.perform(post("/api/v1/auth/validate-token")
				.contentType(MediaType.APPLICATION_JSON)
				.content(validateBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.valid").value(true))
			.andExpect(jsonPath("$.subject").value(email));

		mockMvc.perform(get("/api/v1/auth/me")
				.header("Authorization", "Bearer " + accessToken))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.email").value(email));
	}

	@Test
	void loginWithInvalidCredentialsReturns401() throws Exception {
		String body = objectMapper.writeValueAsString(Map.of(
			"email", "noexiste@donaton.cl",
			"password", "ClaveSegura1"
		));

		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isUnauthorized());
	}

	@Test
	void registerWithInvalidBodyReturns400() throws Exception {
		String body = objectMapper.writeValueAsString(Map.of(
			"email", "correo-invalido",
			"password", "corta",
			"role", "USER"
		));

		mockMvc.perform(post("/api/v1/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isBadRequest());
	}

	@Test
	void duplicateRegisterReturns409() throws Exception {
		String email = "dup.user." + System.nanoTime() + "@donaton.cl";
		String body = objectMapper.writeValueAsString(Map.of(
			"email", email,
			"password", "ClaveSegura1",
			"role", "USER"
		));

		mockMvc.perform(post("/api/v1/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isCreated());

		mockMvc.perform(post("/api/v1/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isConflict());
	}

	@Test
	void validateTokenWithInvalidTokenReturns401() throws Exception {
		String body = objectMapper.writeValueAsString(Map.of("token", "token-invalido"));

		mockMvc.perform(post("/api/v1/auth/validate-token")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isUnauthorized());
	}

	@Test
	void meWithoutTokenReturns401() throws Exception {
		mockMvc.perform(get("/api/v1/auth/me"))
			.andExpect(status().isUnauthorized());
	}
}
