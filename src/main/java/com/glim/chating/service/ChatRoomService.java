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
public class ChatRoomService {

    private final ChatMsgRepository chatMsgRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatUserRepository chatUserRepository;
//    private final UserService userService;

    public List<ViewChatRoomResponse> findChatRoomListByUserId() {
//        Long userId = userService.findByUsername(SecurityUtil.getUsername());
        Long userId = 1L;
        List<ChatUser> chatUserList = chatUserRepository.findAllByUserId(userId).orElseThrow(ErrorCode::throwDummyNotFound);
        if(chatUserList.isEmpty()) {
            ErrorCode.throwDummyNotFound();
        }
        return getViewChatRoomListByUserId(chatUserList);
    }

    private List<ViewChatRoomResponse> getViewChatRoomListByUserId(List<ChatUser> chatUserList) {
        List<ViewChatRoomResponse> chatRoomList = new ArrayList<>();
        User user = null;
        for(ChatUser chatUser : chatUserList) {
            if(chatUser.getValid().toString().equals("OUT")) continue;
            if(user == null) {
//                user = userService.findById(chatUser.getUserId());
            }
            ChatRoom chatRoom = chatRoomRepository.findById(chatUser.getRoomId()).orElseThrow(ErrorCode::throwDummyNotFound);
            ChatMsg chatMsg = chatMsgRepository.findById(chatUser.getRoomId()).orElseThrow(ErrorCode::throwDummyNotFound);
            chatRoomList.add(new ViewChatRoomResponse(chatRoom, chatMsg, user));
        }
        return chatRoomList;
    }

    @Transactional
    public ViewChatRoomResponse createChatRoom(Long joinUserId) {
        ChatRoom chatRoom = new AddChatRoomRequest().toEntity();
        ChatRoom createdRoom = existsChatRoom(joinUserId);
        if(createdRoom == null) {
            createdRoom = chatRoomRepository.save(chatRoom);
            saveChatUser(createdRoom.getId(), joinUserId);
            chatRoomRepository.save(createdRoom);
        }
        User user = null;
//        User user = userService.findById(addChatRoomRequest.getJoinUserId());
        return new ViewChatRoomResponse(chatRoom, user);
    }

    private ChatRoom existsChatRoom(Long joinUserId) {
//        Long userId = userService.findIdByUsername(SecurityUtil.getUsername());
        Long userId = 1L;
        List<ChatUser> userChatUserList = chatUserRepository.findAllByUserId(userId)
                .orElseThrow(ErrorCode::throwDummyNotFound);
        for(ChatUser chatUser : userChatUserList) {
            Long roomId = chatUser.getRoomId();
            ChatUser joinUser = chatUserRepository.findByRoomIdAndUserIdNot(roomId,userId).orElse(null);
            if(joinUser != null && joinUser.getUserId() == joinUserId) {
                return chatRoomRepository.findById(roomId).orElseThrow(ErrorCode::throwDummyNotFound);
            }
        }
        return null;
    }

    private void saveChatUser(Long roomId, Long joinUserId) {
//        Long userId = userService.findIdByUsername(SecurityUtil.getUsername());
        Long userId = 1L;
        AddChatUserRequest addChatUserCreate = new AddChatUserRequest(roomId, userId);
        AddChatUserRequest addChatUserJoin = new AddChatUserRequest(roomId, joinUserId);
        chatUserRepository.save(new AddChatUserRequest().toEntity(addChatUserCreate));
        chatUserRepository.save(new AddChatUserRequest().toEntity(addChatUserJoin));
    }
}
