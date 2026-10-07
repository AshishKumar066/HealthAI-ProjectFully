package com.healthai.hospital_backend.controller;

import com.healthai.hospital_backend.dto.Requests.ChatReq;
import com.healthai.hospital_backend.entity.AppSetting;
import com.healthai.hospital_backend.entity.Notification;
import com.healthai.hospital_backend.service.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/** Dashboard, global search, notifications, settings and the AI assistant. */
@RestController
@RequestMapping("/api")
public class MiscController {
	private final DashboardService dashboard;
	private final SearchService search;
	private final NotificationService notifications;
	private final SettingsService settings;
	private final AiAssistantService ai;

	public MiscController(DashboardService dashboard, SearchService search, NotificationService notifications,
			SettingsService settings, AiAssistantService ai) {
		this.dashboard = dashboard;
		this.search = search;
		this.notifications = notifications;
		this.settings = settings;
		this.ai = ai;
	}

	@GetMapping("/dashboard/stats")
	public Map<String, Object> stats() {
		return dashboard.stats();
	}

	@GetMapping("/search")
	public List<Map<String, Object>> search(@RequestParam String q) {
		return search.search(q);
	}

	@GetMapping("/notifications")
	public List<Notification> notifications() {
		return notifications.latest();
	}

	@GetMapping("/notifications/unread-count")
	public Map<String, Long> unread() {
		return notifications.unreadCount();
	}

	@PatchMapping("/notifications/{id}/read")
	public Notification read(@PathVariable Long id) {
		return notifications.markRead(id);
	}

	@PatchMapping("/notifications/read-all")
	public Map<String, Integer> readAll() {
		return Map.of("updated", notifications.markAllRead());
	}

	@DeleteMapping("/notifications/{id}")
	public Map<String, String> deleteNotification(@PathVariable Long id) {
		notifications.delete(id);
		return Map.of("message", "Deleted");
	}

	@GetMapping("/settings")
	public AppSetting settings() {
		return settings.get();
	}

	@PutMapping("/settings")
	public AppSetting saveSettings(@RequestBody AppSetting s) {
		return settings.update(s);
	}

	@PostMapping("/ai/chat")
	public Map<String, Object> chat(@Valid @RequestBody ChatReq r) {
		return ai.reply(r.message());
	}
}
