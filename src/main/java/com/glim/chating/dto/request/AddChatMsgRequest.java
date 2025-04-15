package com.glim.chating.dto.request;

import com.glim.chating.domain.ChatMsg;
import com.glim.chating.domain.ChatRoom;
import jakarta.persistence.Column;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddChatMsgRequest {
    private Long roomId;
    private Long userId;
    private String content;
    private Long replyMsgId;

    public ChatMsg toEntity(AddChatMsgRequest addChatMsgRequest) {
        return ChatMsg.builder()
                .roomId(addChatMsgRequest.getRoomId())
                .userId(addChatMsgRequest.getUserId())
                .content(addChatMsgRequest.getContent())
                .replyMsgId(addChatMsgRequest.getReplyMsgId())
                .build();
    }
}
