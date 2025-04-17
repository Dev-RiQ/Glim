package com.glim.borad.dto.request;

import com.glim.borad.domain.BoardTags;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddBoardTagRequest {

    private Long tagId;
    private Long boardId;
    private String tag;

    public BoardTags toEntity(AddBoardTagRequest addBoardTagRequest) {
        return BoardTags.builder()
                .boardId(addBoardTagRequest.getBoardId())
                .tag(addBoardTagRequest.getTag())
                .build();
    }
}
