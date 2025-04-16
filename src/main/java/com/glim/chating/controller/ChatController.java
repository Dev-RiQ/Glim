package com.glim.chating.controller;

import com.glim.chating.dto.request.AddChatRoomRequest;
import com.glim.chating.dto.response.PreviewChatMsgResponse;
import com.glim.chating.service.ChatService;
import com.glim.common.kafka.dto.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    
    @PostMapping("/chat")
    public void sendMessage(Message message) {
        message = new Message();
        message.setId(1L);
        message.setRoomId(1L);
        message.setUserId(1L);
        message.setReplyMsgId(0L);
        message.setCreatedAt(LocalDateTime.now());
        message.setContent("test");
        chatService.sendMessage(message);
    }

}
