package com.glim.chating.dto.request;

import com.glim.chating.domain.ChatMsg;
import com.glim.chating.domain.ChatRoom;
import com.glim.common.kafka.dto.Message;
import jakarta.persistence.Column;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddChatMsgRequest {
    private Long roomId;
    private String content;
    private Long replyMsgId;

    public ChatMsg toEntity(AddChatMsgRequest addChatMsgRequest) {
        return ChatMsg.builder()
                .roomId(addChatMsgRequest.getRoomId())
                .content(addChatMsgRequest.getContent())
                .replyMsgId(addChatMsgRequest.getReplyMsgId() == null ? 0L : addChatMsgRequest.getReplyMsgId())
                .build();
    }
}
