package com.glim.borad.dto.response;

import com.glim.borad.domain.BoardComments;
import com.glim.common.utils.CountUtil;
import com.glim.common.utils.DateTimeUtil;
import com.glim.user.dto.response.ViewBoardUserResponse;
import com.glim.user.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
public class ViewReplyCommentResponse {
    private UserRepository userRepository;

    private ViewBoardUserResponse user;
    private Long id;
    private String content;
    private String likes;
    private String createdAt;
    private Long replyCommentId;
    private Boolean isLike;

    public ViewReplyCommentResponse(BoardComments boardComments) {
        this.user = new ViewBoardUserResponse(userRepository.findUserById(boardComments.getUserId()));
        this.id = boardComments.getId();
        this.content = boardComments.getContent();
        this.likes = CountUtil.getCountString(boardComments.getLikes());
        this.createdAt = DateTimeUtil.getDateTimeAgo(boardComments.getCreatedAt());
        this.replyCommentId = boardComments.getReplyCommentId();
    }
}
