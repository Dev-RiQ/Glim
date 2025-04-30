package com.glim.stories.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "story_likes")
@ToString()
public class StoryLikes {
    @Id
    @Column(name = "stroy_id", nullable = false)
    private Long stroyId;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public StoryLikes(Long stroyId, Long userId, LocalDateTime createdAt) {
        this.stroyId = stroyId;
        this.userId = userId;
        this.createdAt = createdAt;
    }
}
