package com.glim.chating.dto.response;

import com.glim.chating.domain.ChatMsg;
import com.glim.chating.domain.ChatRoom;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@ToString
public class ViewChatMsgResponse {
    private Long id;
    private Long userId;
    private String content;
    private Long replyMsgId;
    private LocalTime createdAt;

    public ViewChatMsgResponse(ChatMsg chatMsg) {
        this.id = chatMsg.getId();
        this.userId = chatMsg.getUserId();
        this.content = chatMsg.getContent();
        this.replyMsgId = chatMsg.getReplyMsgId();
        this.createdAt = chatMsg.getCreatedAt().toLocalTime();
    }
}
