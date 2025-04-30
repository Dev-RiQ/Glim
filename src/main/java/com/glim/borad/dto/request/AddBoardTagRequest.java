package com.glim.borad.dto.request;

import com.glim.borad.domain.BoardTags;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddBoardTagRequest {

    private Long boardId;
    private String tag;

    public BoardTags toEntity(Long boardId, String tag) {
        return BoardTags.builder()
                .boardId(boardId)
                .tag(tag)
                .build();
    }
}
