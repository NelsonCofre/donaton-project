package com.donaton.auth.config;

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
	description = "JWT de acceso obtenido desde login o register (header Authorization: Bearer <token>)"
)
public class OpenApiConfig {

	@Bean
	public OpenAPI authOpenApi() {
		return new OpenAPI()
			.info(new Info()
				.title("Donaton – Auth Service")
				.description(
					"API REST de autenticación y gestión de usuarios. "
						+ "Emite y valida tokens JWT, administra refresh tokens y expone el perfil del usuario autenticado."
				)
				.version("1.0.0")
				.contact(new Contact().name("Donaton Team")));
	}
}
