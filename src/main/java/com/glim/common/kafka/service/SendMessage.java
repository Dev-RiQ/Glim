package com.glim.common.kafka.service;

import com.glim.common.kafka.dto.Message;
import com.glim.common.utils.ConstantUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendMessage {
    private final KafkaTemplate<String, Message> kafkaTemplate;

    public void publishMessage(Message message) {
        log.info("message publish : {}", message);
        kafkaTemplate.send(ConstantUtil.KAFKA_TOPIC, message);
    }
}
