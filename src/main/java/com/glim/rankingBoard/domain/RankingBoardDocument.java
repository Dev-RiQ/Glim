package com.glim.rankingBoard.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "ranking_board")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RankingBoardDocument {
    @Id
    private String id;
    private String period;
    private String type;
    private Long boardId;
    private String title;
    private String content;
    private String thumbnailUrl;
    private Integer viewCount;
    private Integer likeCount;
    private LocalDateTime createdDate;
}
