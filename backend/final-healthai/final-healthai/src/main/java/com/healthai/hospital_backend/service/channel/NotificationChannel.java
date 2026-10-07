package com.healthai.hospital_backend.service.channel;

/**
 * Strategy interface: every implementation is a Spring bean created in
 * ChannelConfig.
 */
public interface NotificationChannel {
	String name();

	void send(String type, String title, String message);
}
