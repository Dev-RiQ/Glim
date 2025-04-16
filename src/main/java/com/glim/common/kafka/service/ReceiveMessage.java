package com.glim.common.kafka.service;

import com.glim.common.kafka.dto.Message;
import com.glim.common.utils.ConstantUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReceiveMessage {
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = ConstantUtil.KAFKA_TOPIC, groupId = "glim")
    public void sendMessage(Message response) {
        log.info("message send : {}", response);
        messagingTemplate.convertAndSend("/sub/"+response.getRoomId(), response);
    }
}
