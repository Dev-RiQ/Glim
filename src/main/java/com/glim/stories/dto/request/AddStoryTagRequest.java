package com.glim.stories.dto.request;

import com.glim.stories.domain.StoryTag;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddStoryTagRequest {

    private Long storyId;
    private String tag;

    public StoryTag toEntity(AddStoryTagRequest request) {
        return StoryTag.builder()
                .storyId(storyId)
                .tag(tag)
                .build();
    }
}
