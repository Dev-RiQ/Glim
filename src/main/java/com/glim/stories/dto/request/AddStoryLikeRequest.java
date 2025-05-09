package com.glim.stories.dto.request;

import com.glim.stories.domain.StoryLikes;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddStoryLikeRequest {

    private Long storyId;
    private Long userId;
    private LocalDateTime createdAt;

    public AddStoryLikeRequest(Long storyId, Long id) {
        this.storyId = storyId;
        this.userId = id;
    }

    public StoryLikes toEntity(AddStoryLikeRequest request) {
        return StoryLikes.builder()
                .storyId(request.getStoryId())
                .userId(request.getUserId())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
