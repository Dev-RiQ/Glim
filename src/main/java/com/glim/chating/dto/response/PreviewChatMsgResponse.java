package com.glim.chating.dto.response;

import com.glim.chating.domain.ChatMsg;
import com.glim.chating.domain.ChatRoom;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class PreviewChatMsgResponse {
    private Long id;
    private Long roomId;
    private Long userId;
    private String content;
    private Long replyMsgId;
    private LocalDateTime createdAt;

    public PreviewChatMsgResponse(ChatMsg chatMsg) {
        this.id = chatMsg.getId();
        this.roomId = chatMsg.getRoomId();
        this.userId = chatMsg.getUserId();
        this.content = chatMsg.getContent();
        this.replyMsgId = chatMsg.getReplyMsgId();
        this.createdAt = chatMsg.getCreatedAt();
    }
}
