package com.donaton.bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.donaton.bff.config.BffProperties;

@SpringBootApplication
@EnableConfigurationProperties(BffProperties.class)
public class BffServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BffServiceApplication.class, args);
	}
}
