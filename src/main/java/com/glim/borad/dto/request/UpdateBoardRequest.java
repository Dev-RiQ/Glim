package com.glim.borad.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UpdateBoardRequest {

    private String location;
    private String content;
    private String tagId;
    private LocalDateTime updatedAt = LocalDateTime.now();
    private Boolean viewLikes;
    private Boolean viewShares;
    private Boolean commentable;

}
