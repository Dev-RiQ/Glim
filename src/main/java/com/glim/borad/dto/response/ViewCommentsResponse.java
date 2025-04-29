package com.glim.borad.dto.response;

import com.glim.borad.domain.BoardComments;
import com.glim.common.utils.CountUtil;
import com.glim.common.utils.DateTimeUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
public class ViewCommentsResponse {

    private Long id;
    private Long boardId;
    private Long userId;
    private String content;
    private String likes;
    private String createdAt;
    private String updateAt;
    private Long replyCommentId;
    private Boolean isReply;
    private Boolean isLike;

//    private Long userId;
//    private String content;
//    private String likes;
//    private String createdAt;

//    public ViewCommentsResponse(BoardComments boardComments) {
//        this.userId = boardComments.getUserId();
//        this.content = boardComments.getContent();
//        this.likes = CountUtil.getCountString(boardComments.getLikes());
//        this.createdAt = DateTimeUtil.getDateTimeAgo(boardComments.getCreatedAt());
//    }

    public ViewCommentsResponse(BoardComments boardComments) {
        this.id = boardComments.getId();
        this.boardId = boardComments.getBoardId();
        this.userId = boardComments.getUserId();
        this.content = boardComments.getContent();
        this.likes = CountUtil.getCountString(boardComments.getLikes());
        this.createdAt = DateTimeUtil.getDateTimeAgo(boardComments.getCreatedAt());
        this.updateAt = DateTimeUtil.getDateTimeAgo(boardComments.getUpdateAt());
        this.replyCommentId = boardComments.getReplyCommentId();
    }

}
