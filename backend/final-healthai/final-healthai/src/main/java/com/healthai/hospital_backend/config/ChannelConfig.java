package com.healthai.hospital_backend.config;

import com.healthai.hospital_backend.service.channel.EmailChannel;
import com.healthai.hospital_backend.service.channel.LogChannel;
import com.healthai.hospital_backend.service.channel.NotificationChannel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Java-based configuration: beans are created explicitly with @Bean methods (no
 * XML anywhere).
 */
@Configuration
public class ChannelConfig {

	/** @Primary: wins when someone injects a single NotificationChannel. */
	@Bean
	@Primary
	public NotificationChannel logChannel() {
		return new LogChannel();
	}

	/**
	 * @ConditionalOnProperty: bean exists only if app.channels.email-enabled=true.
	 */
	@Bean
	@ConditionalOnProperty(prefix = "app.channels", name = "email-enabled", havingValue = "true")
	public NotificationChannel emailChannel() {
		return new EmailChannel();
	}
}
