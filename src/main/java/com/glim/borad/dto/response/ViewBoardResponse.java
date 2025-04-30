package com.glim.borad.dto.response;

import com.glim.borad.domain.BoardType;
import com.glim.borad.domain.Boards;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.utils.CountUtil;
import com.glim.common.utils.DateTimeUtil;
import com.glim.user.dto.response.UserResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Setter
public class ViewBoardResponse {

    private final Long id;
    private final String location;
    private  List<String> img = List.of();
    private final String content;
    private final String view;
    private final String like;
    private final String comment;
    private final String share;
    private final String tagId;
    private final String createdAt;
    private final String updatedAt;
    private ViewBgmResponse bgm;
    private UserResponse user;
    private Boolean isLike;
    private Boolean isSave;
    private List<String> tags = List.of();
    private BoardType boardType;

    public ViewBoardResponse(Boards board) {
        this.id = board.getId();
        this.location = board.getLocation();
        this.content = board.getContent();
        this.view = CountUtil.getCountString(board.getViews());
        this.like = CountUtil.getCountString(board.getLikes());
        this.comment = CountUtil.getCountString(board.getComments());
        this.share = CountUtil.getCountString(board.getShares());
        this.tagId = board.getTagUserIds();
        this.createdAt = DateTimeUtil.getDateTimeAgo(board.getCreatedAt());
        this.updatedAt = DateTimeUtil.getDateTimeAgo(board.getUpdatedAt());
        this.boardType = board.getBoardType();
    }

}
