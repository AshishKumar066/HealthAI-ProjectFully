package com.healthai.hospital_backend.service;

import com.healthai.hospital_backend.entity.Notification;
import com.healthai.hospital_backend.exception.ResourceNotFoundException;
import com.healthai.hospital_backend.repository.NotificationRepository;
import com.healthai.hospital_backend.service.channel.NotificationChannel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {
	private final NotificationRepository repo;
	private final List<NotificationChannel> channels; // Spring injects ALL NotificationChannel beans

	public NotificationService(NotificationRepository repo, List<NotificationChannel> channels) {
		this.repo = repo;
		this.channels = channels;
	}

	public void push(String type, String title, String message) {
		Notification n = new Notification();
		n.setType(type);
		n.setTitle(title);
		n.setMessage(message);
		repo.save(n);
		channels.forEach(c -> c.send(type, title, message));
	}

	public List<Notification> latest() {
		return repo.findTop50ByOrderByIdDesc();
	}

	public Map<String, Long> unreadCount() {
		return Map.of("unread", repo.countBySeenFalse());
	}

	public Notification markRead(Long id) {
		Notification n = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notification", id));
		n.setSeen(true);
		return repo.save(n);
	}

	@Transactional
	public int markAllRead() {
		return repo.markAllRead();
	}

	public void delete(Long id) {
		repo.deleteById(id);
	}
}
