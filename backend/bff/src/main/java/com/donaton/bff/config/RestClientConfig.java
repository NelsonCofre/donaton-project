package com.donaton.bff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

	@Bean
	RestClient authRestClient(BffProperties properties) {
		return RestClient.builder()
			.baseUrl(properties.services().auth().baseUrl())
			.build();
	}

	@Bean
	RestClient donationRestClient(BffProperties properties) {
		return RestClient.builder()
			.baseUrl(properties.services().donation().baseUrl())
			.build();
	}
}
