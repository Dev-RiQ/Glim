package com.glim.chating.service;

import com.glim.chating.domain.ChatMsg;
import com.glim.chating.repository.ChatMsgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatUtil {
    private final ChatMsgRepository chatMsgRepository;
    public Long getNextMsgId(){
        ChatMsg chatMsg = chatMsgRepository.findTop1ByOrderByMsgIdDesc().orElse(null);
        return chatMsg == null ? 1L : (chatMsg.getMsgId() + 1);
    }
}
