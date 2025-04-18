package com.glim.stories.dto.response;

import com.glim.common.utils.DateTimeUtil;
import com.glim.stories.domain.Stories;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ViewStoryResponse {

    private Long userId;
    private Integer likes;
    private Integer views;
    private String createdAt;

    public ViewStoryResponse(Stories stories) {
        this.userId = stories.getId();
        this.likes = stories.getLikes();
        this.views = stories.getViews();
        this.createdAt = DateTimeUtil.getDateTimeAgo(stories.getCreatedAt());
    }

}
