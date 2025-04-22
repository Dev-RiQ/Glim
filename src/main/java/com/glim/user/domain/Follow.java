package com.glim.user.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "follow",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"follower_user_id", "following_user_id"})
        }
)
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 팔로우 하는 사람의 user_id (User 엔티티와 관계 없음) */
    @Column(name = "follower_user_id", nullable = false)
    private Long followerUserId;

    /** 팔로우 당하는 사람의 user_id (User 엔티티와 관계 없음) */
    @Column(name = "following_user_id", nullable = false)
    private Long followingUserId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
