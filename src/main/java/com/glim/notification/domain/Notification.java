package com.glim.notification.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString()
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Enumerated(EnumType.STRING)
    private Type type;
    private String message;
    private String uri;
    @Column(name = "send_user_id")
    private Long sendUserId;
    @Column(name = "link_id")
    private Long linkId;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Notification(Long userId, Long sendUserId, String sendUserNickname, Type type, Long linkId) {
        this.userId = userId;
        this.type = type;
        this.sendUserId = sendUserId;
        this.message = sendUserNickname + type.getMessage();
        this.uri = type.getUri();
        this.linkId = linkId;
        this.createdAt = LocalDateTime.now();
    }
}
