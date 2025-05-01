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
    private Long replyId;

    public BoardComments toEntity(AddCommentsRequest addCommentsRequest, Long userId) {
        return BoardComments.builder()
                .boardId(addCommentsRequest.getBoardId())
                .userId(userId)
                .content(addCommentsRequest.getContent())
                .replyId(addCommentsRequest.getReplyId())
                .build();
    }
}
