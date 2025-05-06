package com.glim.borad.dto.request;

import com.glim.borad.domain.Boards;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddBoardRequest {
    private String location;
    private List<String> img;
    private String content;
    private List<String> tagUserIds;
    private List<String> tags;
    private Long bgmId;
    private String boardType;
    private Boolean viewLikes;
    private Boolean commentable;



    public Boards toEntity(AddBoardRequest addBoardRequest, Long userId) {
        return Boards.builder()
                .userId(userId)
                .location(addBoardRequest.getLocation())
                .content(addBoardRequest.getContent())
                .tagUserIds(addBoardRequest.getTagUserIds().toString())
                .bgmId(addBoardRequest.getBgmId())
                .boardType(addBoardRequest.getBoardType())
                .viewLikes(addBoardRequest.getViewLikes())
                .commentable(addBoardRequest.getCommentable())
                .build();

    }
}
