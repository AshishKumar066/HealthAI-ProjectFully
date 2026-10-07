package com.healthai.hospital_backend.service.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simulated e-mail channel (only logs). Replace the body with JavaMailSender to
 * send real e-mails.
 */
public class EmailChannel implements NotificationChannel {
	private static final Logger log = LoggerFactory.getLogger(EmailChannel.class);

	@Override
	public String name() {
		return "email";
	}

	@Override
	public void send(String type, String title, String message) {
		log.info("[EMAIL-SIMULATED] subject='{}' body='{}'", title, message);
	}
}
