package com.glim.borad.dto.response;

import com.glim.borad.domain.BoardType;
import com.glim.borad.domain.Boards;
import com.glim.common.utils.CountUtil;
import com.glim.common.utils.DateTimeUtil;
import com.glim.user.dto.response.ViewBoardUserResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@Setter
public class ViewBoardResponse {

    private ViewBoardUserResponse user;
    private final Long id;
    private final String location;
    private  List<String> img;
    private final String content;
    private final String viewCount;
    private final String likeCount;
    private final String comment;
    private final String tagId;
    private final String createdAt;
    private ViewBgmResponse bgm;
    private Boolean isLike;
    private Boolean isSave;
    private List<String> tags;
    private BoardType boardType;
    private boolean commentable;
    private boolean viewLikes;
    private Boolean isAd;

    public ViewBoardResponse(Boards board, ViewBoardUserResponse user, ViewBgmResponse bgm) {
        this.user = user;
        this.id = board.getId();
        this.location = board.getLocation();
        this.content = board.getContent();
        this.viewCount = CountUtil.getCountString(board.getViews());
        this.likeCount = CountUtil.getCountString(board.getLikes());
        this.comment = CountUtil.getCountString(board.getComments());
        this.tagId = board.getTagUserIds();
        this.bgm = bgm;
        this.createdAt = DateTimeUtil.getDateTimeAgo(board.getCreatedAt());
        this.boardType = board.getBoardType();
        this.img = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.commentable = board.getCommentable().toString().equals("TRUE");
        this.viewLikes = board.getViewLikes().toString().equals("TRUE");
    }

}
