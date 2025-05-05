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
import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.kafka.dto.Message;
import com.glim.common.kafka.service.SendMessage;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.utils.SecurityUtil;
import com.glim.stories.service.StoryService;
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
    private final StoryService storyService;
    private final AwsS3Util awsS3Util;

    public ViewChatUserResponse findByRoomId(Long roomId, SecurityUserDto me) {
        if(!chatUserRepository.existsByRoomIdAndUserId(roomId, me.getId())){
            throw new CustomException(ErrorCode.CHATROOM_NOT_FOUND);
        }
        ChatUser chatUser = chatUserRepository.findByRoomIdAndUserIdNot(roomId, me.getId())
                .orElse(null);
        if(chatUser == null) return null;

        User user = userRepository.findById(chatUser.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.setImg(awsS3Util.getURL(user.getImg(), FileSize.IMAGE_128));
        boolean isStory = storyService.isStory(chatUser.getId());
        return new ViewChatUserResponse(user, chatUser, isStory);
    }

    @Transactional
    public void updateChatUserReadMsg(Long roomId, SecurityUserDto me) {
        ChatUser chatUser = chatUserRepository.findByRoomIdAndUserId(roomId, me.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHATUSER_NOT_FOUND));
        chatUser.update(chatUtil.getNextMsgId() - 1);
        chatUserRepository.save(chatUser);
    }

    @Transactional
    public void escapeChatRoom(Long roomId, SecurityUserDto me) {
        ChatUser chatUser = chatUserRepository.findByRoomIdAndUserId(roomId, me.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHATUSER_NOT_FOUND));
        chatUser.escape();
        chatUserRepository.save(chatUser);
    }

    @Transactional
    public void deleteChatUsersByUser(Long userId) {
        chatUserRepository.deleteByUserId(userId);
    }

    @Transactional
    public void checkUserValid(Long roomId, SecurityUserDto me) {
        ChatUser chatUser = chatUserRepository.findByRoomIdAndUserIdNot(roomId, me.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHATUSER_NOT_FOUND));
        if(chatUser.getValid() == ChatUserValid.OUT) {
            chatUser.reInvite();
            chatUserRepository.save(chatUser);
        }
    }
}
