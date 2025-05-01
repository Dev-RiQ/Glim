package com.glim.stories.dto.response;

import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.utils.CountUtil;
import com.glim.common.utils.DateTimeUtil;
import com.glim.stories.domain.Stories;
import com.glim.user.dto.response.ViewBoardUserResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
public class ViewStoryResponse {

    private ViewBoardUserResponse user;
    private Long id;
    private String createdAt;
    private String img;
    private Boolean isLike;

    public ViewStoryResponse(Stories stories) {
        this.id = stories.getId();
        this.img = stories.getFileName();
        this.createdAt = DateTimeUtil.getDateTimeAgo(stories.getCreatedAt());
    }

}
