package com.healthai.hospital_backend.service.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogChannel implements NotificationChannel {
	private static final Logger log = LoggerFactory.getLogger(LogChannel.class);

	@Override
	public String name() {
		return "log";
	}

	@Override
	public void send(String type, String title, String message) {
		log.info("[NOTIFY:{}] {} - {}", type, title, message);
	}
}
