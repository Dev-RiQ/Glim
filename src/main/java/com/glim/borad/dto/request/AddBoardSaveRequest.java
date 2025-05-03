package com.glim.borad.dto.request;

import com.glim.borad.domain.BoardSaves;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddBoardSaveRequest {

    private Long boardId;
    private Long userId;

    public BoardSaves toEntity(AddBoardSaveRequest addBoardSaveRequest) {
        return BoardSaves.builder()
                .userId(addBoardSaveRequest.getUserId())
                .boardId(addBoardSaveRequest.getBoardId())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
