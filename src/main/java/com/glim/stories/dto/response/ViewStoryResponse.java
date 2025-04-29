package com.glim.stories.dto.response;

import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.utils.CountUtil;
import com.glim.common.utils.DateTimeUtil;
import com.glim.stories.domain.Stories;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
public class ViewStoryResponse {

    private Long storyId;
    private Long userId;
    private String likes;
    private String views;
    private String createdAt;
    private boolean isLike;
    private boolean viewed;

    public ViewStoryResponse(Stories stories) {
        this.storyId = stories.getId();
        this.userId = stories.getId();
        this.likes = CountUtil.getCountString(stories.getLikes());
        this.views = CountUtil.getCountString(stories.getViews());
        this.createdAt = DateTimeUtil.getDateTimeAgo(stories.getCreatedAt());
    }

}
