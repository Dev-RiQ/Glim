package com.glim.borad.dto.response;

import com.glim.borad.domain.Boards;
import com.glim.common.utils.CountUtil;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ViewBoardResponse {

    private Long id;
    private String location;
    private String content;
    private String view;
    private String like;
    private String comment;
    private String share;
    private String tagId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer bgmId;

    public ViewBoardResponse(Boards board) {
        this.id = board.getId();
        this.location = board.getLocation();
        this.content = board.getContent();
        this.view = CountUtil.getCountString(board.getViews());
        this.like = CountUtil.getCountString(board.getLikes());
        this.comment = CountUtil.getCountString(board.getComments());
        this.share = CountUtil.getCountString(board.getShares());
        this.tagId = board.getTagUserIds();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        this.bgmId = board.getBgmId();
    }
}
