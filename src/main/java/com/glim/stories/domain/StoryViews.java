package com.glim.stories.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "story_views")
@ToString()
public class StoryViews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_view_id", nullable = false)
    private Long id;
    @Column(name = "story_id", nullable = false)
    private Long storyId;
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Builder
    public StoryViews(Long storyId ,Long userId) {
        this.storyId = storyId;
        this.userId = userId;
    }
}
