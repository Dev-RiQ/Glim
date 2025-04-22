package com.glim.stories.dto.request;

import com.glim.stories.domain.StoryShare;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddStoryShareRequest {

    private Long storyId;
    private Long userId;

    public StoryShare toEntity(AddStoryShareRequest request) {
        return StoryShare.builder()
                .storyId(request.getStoryId())
                .userId(request.getUserId())
                .build();
    }
}
