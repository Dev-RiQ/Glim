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
public class ChatUserService {

    private final ChatUserRepository chatUserRepository;
    private final ChatMsgService chatMsgService;
//    private final UserService userService;

    public ViewChatUserResponse findByRoomId(Long roomId) {
//        Long userId = userService.findIdByUsername(SecurityUtil.getUsername());
        Long userId = 1L;
        ChatUser chatUser = chatUserRepository.findByRoomIdAndUserIdNot(roomId, userId).orElseThrow(ErrorCode::throwDummyNotFound);
//        return new ViewChatUserResponse(userService.findById(chatUser.getUserId()), chatUser);
        return null;
    }

    @Transactional
    public void updateChatUserReadMsg(Long roomId) {
//        Long userId = userService.findIdByUsername(SecurityUtil.getUsername());
        Long userId = 1L;
        ChatUser chatUser = chatUserRepository.findByRoomIdAndUserId(roomId, userId).orElseThrow(ErrorCode::throwDummyNotFound);
        chatUser.update(chatMsgService.getNextMsgId() - 1);
        chatUserRepository.save(chatUser);
    }

    @Transactional
    public void escapeChatRoom(Long roomId) {
//        Long userId = userService.findIdByUsername(SecurityUtil.getUsername());
        Long userId = 1L;
        ChatUser chatUser = chatUserRepository.findByRoomIdAndUserId(roomId, userId).orElseThrow(ErrorCode::throwDummyNotFound);
        chatUser.escape();
        chatUserRepository.save(chatUser);
    }
}
