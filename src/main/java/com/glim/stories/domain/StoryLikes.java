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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_like_id", nullable = false)
    private Long id;
    @Column(name = "story_id", nullable = false)
    private Long storyId;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public StoryLikes(Long storyId, Long userId, LocalDateTime createdAt) {
        this.storyId = storyId;
        this.userId = userId;
        this.createdAt = createdAt;
    }
}
