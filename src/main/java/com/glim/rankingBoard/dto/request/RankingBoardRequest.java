package com.glim.rankingBoard.dto.request;

import com.glim.borad.domain.BoardType;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingBoardRequest {
    private String period;     // realtime, day, week, month, year
    private BoardType type;    // SHORTS, BASIC
    private String criteria;   // view, like
}
