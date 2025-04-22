package com.glim.stories.dto.response;

import com.glim.common.utils.CountUtil;
import com.glim.common.utils.DateTimeUtil;
import com.glim.stories.domain.Stories;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ViewStoryResponse {

    private Long userId;
    private String likes;
    private String views;
    private String createdAt;

    public ViewStoryResponse(Stories stories) {
        this.userId = stories.getId();
        this.likes = CountUtil.getCountString(stories.getLikes());
        this.views = CountUtil.getCountString(stories.getViews());
        this.createdAt = DateTimeUtil.getDateTimeAgo(stories.getCreatedAt());
    }

}
