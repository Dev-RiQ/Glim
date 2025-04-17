package com.glim.borad.dto.request;

import com.glim.borad.domain.BoardSaves;
import com.glim.borad.domain.BoardShare;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddBoardShareRequest {
    private Long userId;
    private Long boardId;

    public BoardShare toEntity(AddBoardShareRequest addBoardShareRequest) {
        return BoardShare.builder()
                .userId(addBoardShareRequest.getUserId())
                .boardId(addBoardShareRequest.getBoardId())
                .build();
    }
}
