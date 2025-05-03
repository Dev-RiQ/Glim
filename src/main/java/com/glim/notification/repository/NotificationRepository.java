package com.glim.notification.repository;

import com.glim.notification.domain.Notification;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserIdAndIdGreaterThan(Long userId, Long id);
    void deleteByUserIdOrSendUserId(Long userId, Long sendUserId);

    List<Notification> findAllByUserIdAndIdLessThanOrderByIdDesc(Long id, Long readAlarmId, Limit of);

    List<Notification> findAllByUserIdAndIdLessThanAndIdLessThanOrderByIdDesc(Long id, Long readAlarmId, Long offset, Limit of);

    Boolean existsByUserIdAndIdGreaterThan(Long userId, Long readAlarmId);
}
