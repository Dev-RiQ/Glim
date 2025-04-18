package com.glim.stories.dto.request;

import com.glim.stories.domain.StoryViews;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class AddStoryViewRequest {

    private Long storyId;
    private Long userId;

    public StoryViews toEntity(AddStoryViewRequest request) {
        return StoryViews.builder()
                .storyId(request.getStoryId())
                .userId(request.getUserId())
                .build();
    }
}
