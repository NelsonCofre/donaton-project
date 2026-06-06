package com.donaton.bff.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record BffProperties(Services services, Cors cors) {

	public record Services(Service auth, Service donation) {
	}

	public record Service(String baseUrl) {
	}

	public record Cors(String allowedOrigins) {
	}
}
