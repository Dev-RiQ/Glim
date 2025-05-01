package com.glim.borad.dto.response;

import com.glim.borad.domain.Boards;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Setter
public class ViewMyPageBoardResponse {

    private Long id;
    private List<String> img = List.of();

    public ViewMyPageBoardResponse(Boards board) {
        this.id = board.getId();
    }
}
