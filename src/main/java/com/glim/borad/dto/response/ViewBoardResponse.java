package com.glim.borad.dto.response;

import com.glim.borad.domain.Boards;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ViewBoardResponse {

    private Long id;
    private String location;
    private String content;
    private Integer view;
    private Integer like;
    private Integer comment;
    private Integer share;
    private String tagId;
    private String createdAt;
    private String updatedAt;
    private Integer bgmId;

    public ViewBoardResponse(Boards board) {
        this.id = board.getId();
        this.location = board.getLocation();
        this.content = board.getContent();
        this.view = board.getViews();
        this.like = board.getLikes();
        this.comment = board.getComments();
        this.share = board.getShares();
        this.tagId = board.getTagUserIds();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        this.bgmId = board.getBgmId();
    }
}
