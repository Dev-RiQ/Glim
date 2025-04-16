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
    private String updatedAt = String.valueOf(LocalDateTime.now());
    private Boolean viewLike;
    private Boolean viewShare;
    private Boolean commentable;

}
