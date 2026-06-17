package com.donaton.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CustomUserDetailsService customUserDetailsService;
	private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

	public SecurityConfig(
		JwtAuthenticationFilter jwtAuthenticationFilter,
		CustomUserDetailsService customUserDetailsService,
		RestAuthenticationEntryPoint restAuthenticationEntryPoint
	) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.customUserDetailsService = customUserDetailsService;
		this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.exceptionHandling(exception -> exception.authenticationEntryPoint(restAuthenticationEntryPoint))
			.authenticationProvider(authenticationProvider())
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(
					"/swagger-ui/**",
					"/swagger-ui.html",
					"/v3/api-docs/**"
				).permitAll()
				.requestMatchers(HttpMethod.POST, "/api/v1/auth/register", "/api/v1/auth/login", "/api/v1/auth/validate-credentials", "/api/v1/auth/validate-token", "/api/v1/auth/refresh-token").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/v1/auth/logout").authenticated()
				.requestMatchers(HttpMethod.GET, "/api/v1/auth/me").authenticated()
				.anyRequest().authenticated()
			)
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(customUserDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}
