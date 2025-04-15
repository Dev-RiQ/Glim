package com.glim.chating.service;

import com.glim.chating.domain.ChatRoom;
import com.glim.chating.dto.request.AddChatRoomRequest;
import com.glim.chating.dto.response.PreviewChatMsgResponse;
import com.glim.chating.repository.ChatMsgRepository;
import com.glim.common.exception.ErrorCode;
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

    public PreviewChatMsgResponse findDummyById(Long id) {
        return new PreviewChatMsgResponse(chatMsgRepository.findById(id).orElseThrow(ErrorCode::throwUserNotFound));
    }

    @Transactional
    public void insert(AddChatRoomRequest request) {
        chatMsgRepository.save(new AddChatRoomRequest().toEntity(request));
    }

    @Transactional
    public void update(Long id, UpdateChatMsgRequest request) {
        ChatRoom chatRoom = chatMsgRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        chatRoom.update(request);
        chatMsgRepository.save(chatRoom);
    }

    @Transactional
    public void delete(Long id) {
        ChatRoom chatRoom = chatMsgRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        chatMsgRepository.delete(chatRoom);
    }
}
