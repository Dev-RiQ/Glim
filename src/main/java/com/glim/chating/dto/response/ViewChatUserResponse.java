package com.glim.chating.dto.response;

import com.glim.chating.domain.ChatRoom;
import com.glim.chating.domain.ChatUser;
import com.glim.chating.domain.ChatUserValid;
import com.glim.common.utils.DateTimeUtil;
import com.glim.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@Setter
public class ViewChatUserResponse {
    private Long id;
    private String img;
    private String nickname;
    private Long readMsgId;
    private ChatUserValid valid;
    private Boolean isStory;

    public ViewChatUserResponse(User user, ChatUser chatUser, boolean isStory) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.img = user.getImg();
        this.readMsgId = chatUser.getReadMsgId();
        this.valid = chatUser.getValid();
        this.isStory = isStory;
    }
}
