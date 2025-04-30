package com.glim.rankingBoard.dto.response;

import com.glim.common.utils.DateTimeUtil;
import com.glim.rankingBoard.domain.RankingBoardDocument;
import lombok.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingBoardResponse {

    private Long boardId;           // 게시글 ID
    private String content;         // 게시글 내용
    private String contentImgUrl;   // 게시글 썸네일 or 첫 사진 링크
    private Integer viewCount;      // 조회수
    private Integer likeCount;      // 좋아요 수
    private String createdAt; // 작성일

    public static RankingBoardResponse from(RankingBoardDocument document, String contentImgUrl) {
        return RankingBoardResponse.builder()
                .boardId(document.getBoardId())
                .content(document.getContent())
                .contentImgUrl(contentImgUrl)
                .viewCount(document.getViewCount())
                .likeCount(document.getLikeCount())
                .createdAt(DateTimeUtil.getDateTimeAgo(document.getCreatedAt())) 
                .build();
    }
}
