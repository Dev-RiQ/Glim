package com.glim.rankingBoard.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingBoardRequest {
    private String period;
    private String type;
    private String criteria;
}
