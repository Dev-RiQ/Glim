package com.glim.borad.dto.request;

import com.glim.borad.domain.CommentLikes;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddCommentLikeRequest {

    private Long commentId;
    private Long userId;

    public CommentLikes toEntity(AddCommentLikeRequest addCommentLikeRequest) {
        return CommentLikes.builder()
                .commentId(addCommentLikeRequest.getCommentId())
                .userId(addCommentLikeRequest.getUserId())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
