package com.donaton.bff.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	private final BffProperties properties;

	public CorsConfig(BffProperties properties) {
		this.properties = properties;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**")
			.allowedOrigins(properties.cors().allowedOrigins().split(","))
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
			.allowedHeaders("*")
			.allowCredentials(true);
	}
}
