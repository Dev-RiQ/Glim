package com.glim.rankingBoard.dto.response;

import com.glim.rankingBoard.domain.RankingBoardDocument;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingBoardResponse {
    private Long boardId;
    private String title;
    private String content;
    private String thumbnailUrl;
    private Integer viewCount;
    private Integer likeCount;
    private LocalDateTime createdDate;

    public static RankingBoardResponse from(RankingBoardDocument document) {
        return RankingBoardResponse.builder()
                .boardId(document.getBoardId())
                .title(document.getTitle())
                .content(document.getContent())
                .thumbnailUrl(document.getThumbnailUrl())
                .viewCount(document.getViewCount())
                .likeCount(document.getLikeCount())
                .createdDate(document.getCreatedDate())
                .build();
    }
}
