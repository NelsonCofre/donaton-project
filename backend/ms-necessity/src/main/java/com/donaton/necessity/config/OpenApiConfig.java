package com.donaton.necessity.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI necessityOpenApi() {
		return new OpenAPI()
			.info(new Info()
				.title("Donaton – Necessity Service")
				.description(
					"API REST del microservicio de necesidades en terreno. "
						+ "Permite registrar, consultar, actualizar y eliminar necesidades reportadas "
						+ "por municipalidades y centros investigadores."
				)
				.version("1.0.0")
				.contact(new Contact().name("Donaton Team")));
	}
}
