package com.donaton.donation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI donationOpenApi() {
		return new OpenAPI()
			.info(new Info()
				.title("Donaton – Donation Service")
				.description(
					"API REST del microservicio de donaciones. "
						+ "Permite registrar, consultar, actualizar y eliminar donaciones de recursos "
						+ "con origen, fecha y centro de acopio asignado."
				)
				.version("1.0.0")
				.contact(new Contact().name("Donaton Team")));
	}
}
