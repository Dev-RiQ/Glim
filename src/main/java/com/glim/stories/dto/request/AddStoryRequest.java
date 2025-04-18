package com.glim.stories.dto.request;

import com.glim.stories.domain.Stories;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddStoryRequest {

    private Long userId;
    private String fileName;
    private Integer likes;
    private Integer views;
    private LocalDateTime createdAt;

    public Stories toEntity(AddStoryRequest addStoryRequest) {
        return Stories.builder()
                .userId(addStoryRequest.getUserId())
                .fileName(addStoryRequest.getFileName())
                .likes(addStoryRequest.getLikes())
                .views(addStoryRequest.getViews())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
