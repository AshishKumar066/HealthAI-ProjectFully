package com.healthai.hospital_backend.repository;

import com.healthai.hospital_backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findTop50ByOrderByIdDesc();

	long countBySeenFalse();

	@Modifying
	@Query("update Notification n set n.seen = true where n.seen = false")
	int markAllRead();
}
