package com.donaton.bff.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.donaton.bff.security.JwtAuthFilter;

@Configuration
public class FilterConfig {

	@Bean
	FilterRegistrationBean<JwtAuthFilter> jwtAuthFilterRegistration(JwtAuthFilter filter) {
		FilterRegistrationBean<JwtAuthFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(filter);
		registration.addUrlPatterns(
			"/api/donations",
			"/api/donations/*",
			"/api/v1/necessities",
			"/api/v1/necessities/*",
			"/api/v1/logistics",
			"/api/v1/logistics/*"
		);
		registration.setOrder(1);
		return registration;
	}
}
