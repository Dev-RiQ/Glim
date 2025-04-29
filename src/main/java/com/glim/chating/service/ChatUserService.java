package com.glim.chating.service;

import com.glim.chating.domain.ChatMsg;
import com.glim.chating.domain.ChatRoom;
import com.glim.chating.domain.ChatUser;
import com.glim.chating.domain.ChatUserValid;
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
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.utils.SecurityUtil;
import com.glim.user.domain.User;
import com.glim.user.repository.UserRepository;
import com.glim.user.service.UserService;
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
    private final ChatUtil chatUtil;
    private final UserRepository userRepository;
//    private final UserService userService;

    public ViewChatUserResponse findByRoomId(Long roomId) {
        SecurityUserDto user = SecurityUtil.getUser();
        ChatUser chatUser = chatUserRepository.findByRoomIdAndUserIdNot(roomId, user.getId()).orElseThrow(ErrorCode::throwDummyNotFound);
        return new ViewChatUserResponse(userRepository.findById(chatUser.getUserId()).orElseThrow(ErrorCode::throwDummyNotFound), chatUser);
    }

    @Transactional
    public void updateChatUserReadMsg(Long roomId) {
        SecurityUserDto user = SecurityUtil.getUser();
        ChatUser chatUser = chatUserRepository.findByRoomIdAndUserId(roomId, user.getId()).orElseThrow(ErrorCode::throwDummyNotFound);
        chatUser.update(chatUtil.getNextMsgId() - 1);
        chatUserRepository.save(chatUser);
    }

    @Transactional
    public void escapeChatRoom(Long roomId) {
        SecurityUserDto user = SecurityUtil.getUser();
        ChatUser chatUser = chatUserRepository.findByRoomIdAndUserId(roomId, user.getId()).orElseThrow(ErrorCode::throwDummyNotFound);
        chatUser.escape();
        chatUserRepository.save(chatUser);
    }

    @Transactional
    public void deleteChatUsersByUser(Long userId) {
        chatUserRepository.deleteByUserId(userId);
    }

    @Transactional
    public void checkUserValid(Long roomId) {
        ChatUser chatUser = chatUserRepository.findById(roomId).orElseThrow(ErrorCode::throwDummyNotFound);
        if(chatUser.getValid() == ChatUserValid.OUT) {
            chatUser.reInvite();
            chatUserRepository.save(chatUser);
        }
    }
}
