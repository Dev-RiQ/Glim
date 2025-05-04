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
import com.glim.common.exception.CustomException;
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
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMsgService {

    private final ChatMsgRepository chatMsgRepository;
    private final ChatUserService chatUserService;
    private final ChatUserRepository chatUserRepository;
    private final SendMessage sender;
    private final ChatUtil chatUtil;

    public List<ViewChatMsgResponse> findChatMsgListByRoomId(Long roomId, Long offset) {
        ChatUser chatUser = chatUserRepository.findByRoomIdAndUserId(roomId, SecurityUtil.getUser().getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Long loadMin = 0L;
        if(chatUser.getOutMsgId() != null && chatUser.getOutMsgId() != 0){
            loadMin = chatUser.getOutMsgId();
        }
        List<ChatMsg> chatMsgList = offset == null ?
            chatMsgRepository.findAllByRoomIdAndMsgIdGreaterThanOrderByMsgIdDesc(roomId, loadMin ,Limit.of(30))
                    .orElseThrow(() -> new CustomException(ErrorCode.CHATMSG_NOT_FOUND))
            : chatMsgRepository.findAllByRoomIdAndMsgIdBetweenOrderByMsgIdDesc(roomId, loadMin, offset, Limit.of(30))
                    .orElseThrow(() -> new CustomException(ErrorCode.CHATMSG_NO_MORE));
        if(chatMsgList == null || chatMsgList.isEmpty()) {
            return Collections.emptyList();
        }
        Collections.reverse(chatMsgList);
        return chatMsgList.stream().map(ViewChatMsgResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public void sendMessage(AddChatMsgRequest request) {
        ChatMsg message = setMessage(request);
        log.info("send message : {}", message);
        chatUserService.checkUserValid(request.getRoomId());
        chatMsgRepository.save(message);
        sender.publishMessage(new Message(message));
    }

    private ChatMsg setMessage(AddChatMsgRequest addChatMsgRequest) {
        ChatMsg message = new AddChatMsgRequest().toEntity(addChatMsgRequest);
        SecurityUserDto user = SecurityUtil.getUser();
        message.setUserId(user.getId());
        message.setMsgId(chatUtil.getNextMsgId());
        message.setCreatedAt(LocalDateTime.now());
        return message;
    }

}
