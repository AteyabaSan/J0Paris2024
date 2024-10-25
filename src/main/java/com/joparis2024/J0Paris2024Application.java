package com.joparis2024;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

import com.joparis2024.config.StripeProperties;

@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties(StripeProperties.class)
public class J0Paris2024Application {

	public static void main(String[] args) {
		SpringApplication.run(J0Paris2024Application.class, args);
	}

}
