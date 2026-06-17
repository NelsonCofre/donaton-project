package com.donaton.logistics.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI logisticsOpenApi() {
		return new OpenAPI()
			.info(new Info()
				.title("Donaton – Logistics Service")
				.description(
					"API REST del microservicio de logística. "
						+ "Gestiona centros de acopio, inventario por centro y envíos de donaciones."
				)
				.version("1.0.0")
				.contact(new Contact().name("Donaton Team")));
	}
}
