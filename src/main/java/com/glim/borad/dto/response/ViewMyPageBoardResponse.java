package com.glim.borad.dto.response;

import com.glim.borad.domain.BoardType;
import com.glim.borad.domain.Boards;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@Setter
public class ViewMyPageBoardResponse {

    private Long id;
    private String img;
    private BoardType type;

    public ViewMyPageBoardResponse(Boards board, String img) {
        this.id = board.getId();
        this.type = board.getBoardType();
        this.img = img;
    }
}
