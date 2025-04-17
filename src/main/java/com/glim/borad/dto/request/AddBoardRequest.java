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
    private String tagUserIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer bgmId;
    private Boolean boardType;
    private Boolean viewLikes;
    private Boolean viewShares;
    private Boolean commentable;

    public Boards toEntity(AddBoardRequest addBoardRequest) {
        return Boards.builder()
                .userId(addBoardRequest.getUserId())
                .location(addBoardRequest.getLocation())
                .content(addBoardRequest.getContent())
                .tagUserIds(addBoardRequest.getTagUserIds())
                .bgmId(addBoardRequest.getBgmId())
                .boardType(addBoardRequest.getBoardType())
                .viewLikes(addBoardRequest.getViewLikes())
                .viewShares(addBoardRequest.getViewShares())
                .commentable(addBoardRequest.getCommentable())
                .build();
    }
}
