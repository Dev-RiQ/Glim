package com.glim.borad.dto.request;

import com.glim.borad.domain.BoardLikes;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddBoardLikeRequest {

    private Long boardId;
    private Long userId;

    public BoardLikes toEntity(AddBoardLikeRequest addBoardLikeRequest) {
        return BoardLikes.builder()
                .boardId(addBoardLikeRequest.getBoardId())
                .userId(addBoardLikeRequest.getUserId())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
