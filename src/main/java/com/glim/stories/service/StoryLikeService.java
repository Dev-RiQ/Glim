package com.glim.stories.service;

import com.glim.borad.repository.BoardRepository;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.notification.domain.Type;
import com.glim.notification.service.NotificationService;
import com.glim.stories.domain.StoryLikes;
import com.glim.stories.dto.request.AddStoryLikeRequest;
import com.glim.stories.repository.StoryLikeRepository;
import com.glim.stories.repository.StoryRepository;
import com.glim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoryLikeService {

    private final StoryLikeRepository storyLikeRepository;
    private final NotificationService notificationService;
    private final StoryRepository storyRepository;

    @Transactional
    public void insert(AddStoryLikeRequest request, SecurityUserDto user) {
        storyLikeRepository.save(new AddStoryLikeRequest().toEntity(request));
        Long notificationUserId = storyRepository.findById(request.getStoryId()).orElseThrow(() -> new CustomException(ErrorCode.STORY_DELETED)).getUserId();
        notificationService.send(notificationUserId, Type.STORY_LIKE, request.getStoryId(), user);
    }

    @Transactional
    public void delete(Long storyId, Long userId, SecurityUserDto user) {
        StoryLikes storyLike = storyLikeRepository.findByStoryIdAndUserId(storyId,userId).orElseThrow(() -> new CustomException(ErrorCode.DUPLICATE_STORY_LIKE));
        storyLikeRepository.deleteById(storyLike.getId());
        Long notificationUserId = storyRepository.findById(storyId).orElseThrow(() -> new CustomException(ErrorCode.DUPLICATE_STORY_LIKE)).getUserId();
        notificationService.delete(notificationUserId, Type.STORY_LIKE, storyId, user);
    }
}
