package com.glim.stories.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "story_tags")
@ToString()
public class StoryTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_tag_id", nullable = false)
    private Long id;
    @Column(name = "story_id", nullable = false)
    private Long storyId;
    @Column(nullable = false)
    private String tag;

    @Builder
    public StoryTag(Long storyId, String tag) {
        this.storyId = storyId;
        this.tag = tag;
    }
}
