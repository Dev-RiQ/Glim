package com.glim.borad.dto.request;

import com.glim.borad.domain.BoardComments;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddCommentsRequest {

    private Long boardId;
    private Long userId;
    private String content;
    private Integer likes;
    private Long replyId;

    public BoardComments toEntity(AddCommentsRequest addCommentsRequest) {
        return BoardComments.builder()
                .boardId(addCommentsRequest.getBoardId())
                .userId(addCommentsRequest.getUserId())
                .content(addCommentsRequest.getContent())
                .likes(addCommentsRequest.getLikes())
                .replyId(addCommentsRequest.getReplyId())
                .build();
    }
}
