package com.glim.stories.dto.response;

import com.glim.borad.domain.BoardType;
import com.glim.borad.domain.Boards;
import com.glim.stories.domain.Stories;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
public class ViewMyPageStoryResponse {

    private Long id;
    private String img;

    public ViewMyPageStoryResponse(Stories story) {
        this.id = story.getId();
        this.img = story.getFileName();
    }
}
