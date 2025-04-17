package com.glim.borad.dto.request;

import com.glim.borad.domain.BoardViews;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddBoardViewRequest {

    private Long boardId;
    private Long userId;

    public BoardViews toEntity(AddBoardViewRequest request) {
        return BoardViews.builder()
                .boardId(request.getBoardId())
                .userId(request.getUserId())
                .build();
    }
}
