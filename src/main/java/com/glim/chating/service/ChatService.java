package com.glim.chating.service;

import com.glim.chating.repository.ChatMsgRepository;
import com.glim.chating.repository.ChatRoomRepository;
import com.glim.chating.repository.ChatUserRepository;
import com.glim.common.kafka.dto.Message;
import com.glim.common.kafka.service.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatMsgRepository chatMsgRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatUserRepository chatUserRepository;
    private final SendMessage sender;

    public void sendMessage(Message message) {
        log.info("send message : {}", message);
        sender.publishMessage(message);
    }

//    public PreviewChatMsgResponse findDummyById(Long id) {
//        return new PreviewChatMsgResponse(chatMsgRepository.findById(id).orElseThrow(ErrorCode::throwUserNotFound));
//    }
//
//    @Transactional
//    public void insert(AddChatRoomRequest request) {
//        chatMsgRepository.save(new AddChatRoomRequest().toEntity(request));
//    }
//
//    @Transactional
//    public void update(Long id, UpdateChatMsgRequest request) {
//        ChatRoom chatRoom = chatMsgRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
//        chatRoom.update(request);
//        chatMsgRepository.save(chatRoom);
//    }
//
//    @Transactional
//    public void delete(Long id) {
//        ChatRoom chatRoom = chatMsgRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
//        chatMsgRepository.delete(chatRoom);
//    }
}
