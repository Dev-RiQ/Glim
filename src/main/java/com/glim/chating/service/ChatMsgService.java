package com.glim.chating.service;

import com.glim.chating.domain.ChatMsg;
import com.glim.chating.domain.ChatRoom;
import com.glim.chating.domain.ChatUser;
import com.glim.chating.dto.request.AddChatMsgRequest;
import com.glim.chating.dto.request.AddChatRoomRequest;
import com.glim.chating.dto.request.AddChatUserRequest;
import com.glim.chating.dto.response.ViewChatMsgResponse;
import com.glim.chating.dto.response.ViewChatRoomResponse;
import com.glim.chating.dto.response.ViewChatUserResponse;
import com.glim.chating.repository.ChatMsgRepository;
import com.glim.chating.repository.ChatRoomRepository;
import com.glim.chating.repository.ChatUserRepository;
import com.glim.common.exception.ErrorCode;
import com.glim.common.kafka.dto.Message;
import com.glim.common.kafka.service.SendMessage;
import com.glim.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMsgService {

    private final ChatMsgRepository chatMsgRepository;
    private final SendMessage sender;
//    private final UserService userService;

    public List<ViewChatMsgResponse> findChatMsgListByRoomId(Long roomId, Long offset) {
        List<ChatMsg> chatMsgList = offset == null ?
            chatMsgRepository.findAllByRoomIdOrderByMsgIdDesc(roomId, Limit.of(30))
            : chatMsgRepository.findAllByRoomIdAndMsgIdLessThanOrderByMsgIdDesc(roomId, offset, Limit.of(30));
        if(chatMsgList == null || chatMsgList.isEmpty()) {
            ErrorCode.throwDummyNotFound();
        }
        Collections.reverse(chatMsgList);
        return chatMsgList.stream().map(ViewChatMsgResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public void sendMessage(AddChatMsgRequest request) {
        ChatMsg message = setMessage(request);
        log.info("send message : {}", message);
        chatMsgRepository.save(message);
        sender.publishMessage(new Message(message));
    }

    private ChatMsg setMessage(AddChatMsgRequest addChatMsgRequest) {
        ChatMsg message = new AddChatMsgRequest().toEntity(addChatMsgRequest);
//        Long userId = userService.findIdByUsername(SecurityUtil.getUsername());
//        message.setUserId(userId);
        message.setUserId(1L);
        message.setMsgId(getNextMsgId());
        message.setCreatedAt(LocalDateTime.now());
        return message;
    }

    public Long getNextMsgId(){
        ChatMsg chatMsg = chatMsgRepository.findTop1ByOrderByMsgIdDesc().orElse(null);
        return chatMsg == null ? 1L : (chatMsg.getMsgId() + 1);
    }
}
