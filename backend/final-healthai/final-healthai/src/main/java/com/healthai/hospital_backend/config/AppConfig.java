package com.healthai.hospital_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Clock;
import java.time.ZoneId;

/** Central Java configuration class for application-wide beans. */
@Configuration
@EnableConfigurationProperties(AppProperties.class) // registers AppProperties as a bean
public class AppConfig {

	/**
	 * One Clock bean used everywhere instead of LocalDateTime.now() -> easy to
	 * test, one timezone.
	 */
	@Bean
	public Clock clock(@Value("${app.timezone:Asia/Kolkata}") String zone) {
		return Clock.system(ZoneId.of(zone));
	}

	/** @Profile: only created when running with --spring.profiles.active=dev */
	@Bean
	@Profile("dev")
	public CommandLineRunner devNotice() {
		return args -> System.out.println(">>> Running with profile 'dev'");
	}
}
