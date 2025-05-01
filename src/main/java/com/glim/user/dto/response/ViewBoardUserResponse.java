package com.glim.user.dto.response;

import com.glim.user.domain.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
public class ViewBoardUserResponse {

    private Long id;
    private String nickname;
    private String img;
    private Boolean isStory;
    private Boolean isMine;

    public ViewBoardUserResponse(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.img = user.getImg();
    }
}
