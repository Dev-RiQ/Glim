package com.glim.notification.repository;

import com.glim.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserIdAndIdGreaterThan(Long userId, Long id);
}
