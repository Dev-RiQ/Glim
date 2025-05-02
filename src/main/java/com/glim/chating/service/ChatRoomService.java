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
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.utils.SecurityUtil;
import com.glim.user.domain.User;
import com.glim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private final UserRepository userRepository;

    public List<ViewChatRoomResponse> findChatRoomListByUserId() {
        SecurityUserDto user = SecurityUtil.getUser();
        List<ChatUser> chatUserList = chatUserRepository.findAllByUserId(user.getId()).orElseThrow(ErrorCode::throwDummyNotFound);
        return getViewChatRoomListByUserId(chatUserList);
    }

    private List<ViewChatRoomResponse> getViewChatRoomListByUserId(List<ChatUser> chatUserList) {
        List<ViewChatRoomResponse> chatRoomList = new ArrayList<>();
        User user = null;
        for(ChatUser chatUser : chatUserList) {
            if(chatUser.getValid().toString().equals("OUT")) continue;
            if(user == null) {
                user = userRepository.findById(chatUser.getUserId()).orElseThrow(ErrorCode::throwDummyNotFound);
            }
            ChatRoom chatRoom = chatRoomRepository.findById(chatUser.getRoomId()).orElseThrow(ErrorCode::throwDummyNotFound);
            ChatMsg chatMsg = chatMsgRepository.findById(chatUser.getRoomId()).orElseThrow(ErrorCode::throwDummyNotFound);
            Long readId = chatUserRepository.findByRoomIdAndUserId(chatUser.getRoomId(), chatUser.getId()).orElseThrow(ErrorCode::throwDummyNotFound).getReadMsgId();
            Long lastId = chatMsgRepository.findTop1ByRoomIdOrderByMsgIdDesc(chatUser.getRoomId()).orElseThrow(ErrorCode::throwDummyNotFound).getMsgId();
            boolean hasRead = readId.equals(lastId);
            chatRoomList.add(new ViewChatRoomResponse(chatRoom, chatMsg, user, hasRead));
        }
        return chatRoomList.stream().sorted(Comparator.comparing(ViewChatRoomResponse::getUpdatedAt)).toList();
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
        User user = userRepository.findById(joinUserId).orElseThrow(ErrorCode::throwDummyNotFound);
        return new ViewChatRoomResponse(chatRoom, user);
    }

    private ChatRoom existsChatRoom(Long joinUserId) {
        SecurityUserDto user = SecurityUtil.getUser();
        List<ChatUser> userChatUserList = chatUserRepository.findAllByUserId(user.getId())
                .orElseThrow(ErrorCode::throwDummyNotFound);
        for(ChatUser chatUser : userChatUserList) {
            Long roomId = chatUser.getRoomId();
            ChatUser joinUser = chatUserRepository.findByRoomIdAndUserIdNot(roomId,user.getId()).orElse(null);
            if(joinUser != null && joinUser.getUserId() == joinUserId) {
                return chatRoomRepository.findById(roomId).orElseThrow(ErrorCode::throwDummyNotFound);
            }
        }
        return null;
    }

    private void saveChatUser(Long roomId, Long joinUserId) {
        SecurityUserDto user = SecurityUtil.getUser();
        AddChatUserRequest addChatUserCreate = new AddChatUserRequest(roomId, user.getId());
        AddChatUserRequest addChatUserJoin = new AddChatUserRequest(roomId, joinUserId);
        chatUserRepository.save(new AddChatUserRequest().toEntity(addChatUserCreate));
        chatUserRepository.save(new AddChatUserRequest().toEntity(addChatUserJoin));
    }
}
