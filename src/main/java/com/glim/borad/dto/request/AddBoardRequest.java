package com.glim.borad.dto.request;

import com.glim.borad.domain.Boards;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddBoardRequest {
    private Long userId;
    private String location;
    private String content;
    private String tagUserId;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private Integer bgmId;
    private Boolean boardType;
    private Boolean viewLike;
    private Boolean viewShares;
    private Boolean commentable;

    public Boards toEntity(AddBoardRequest addBoardRequest) {
        return Boards.builder()
                .userId(addBoardRequest.getUserId())
                .location(addBoardRequest.getLocation())
                .content(addBoardRequest.getContent())
                .tagUserIds(addBoardRequest.getTagUserId())
                .createdAt(String.valueOf(addBoardRequest.getCreatedAt()))
                .updatedAt(String.valueOf(addBoardRequest.getUpdatedAt()))
                .bgmId(addBoardRequest.getBgmId())
                .boardType(addBoardRequest.getBoardType())
                .viewLikes(addBoardRequest.getViewLike())
                .viewShares(addBoardRequest.getViewShares())
                .commentable(addBoardRequest.getCommentable())
                .build();
    }
}
