package com.donaton.bff.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
	name = "bearerAuth",
	type = SecuritySchemeType.HTTP,
	scheme = "bearer",
	bearerFormat = "JWT",
	description = "Token JWT obtenido desde POST /api/auth/login (header Authorization: Bearer <token>)"
)
public class OpenApiConfig {

	@Bean
	public OpenAPI bffOpenApi() {
		return new OpenAPI()
			.info(new Info()
				.title("Donaton – BFF (Frontend API)")
				.description(
					"API HTTP consumida por el frontend React. Orquesta llamadas a microservicios internos "
						+ "y adapta requests/responses al contrato de la UI. "
						+ "Integración actual: autenticación y donaciones. "
						+ "Necesidades y logística: pendientes de integración."
				)
				.version("1.0.0")
				.contact(new Contact().name("Donaton Team")));
	}
}
