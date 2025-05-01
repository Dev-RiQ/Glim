package com.glim.stories.service;

import com.glim.stories.domain.StoryLikes;
import com.glim.stories.dto.request.AddStoryLikeRequest;
import com.glim.stories.repository.StoryLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoryLikeService {

    private final StoryLikeRepository storyLikeRepository;

    @Transactional
    public void insert(AddStoryLikeRequest request) {
        storyLikeRepository.save(new AddStoryLikeRequest().toEntity(request));
    }

    @Transactional
    public void delete(Long storyId, Long userId) {
        StoryLikes storyLike = storyLikeRepository.findByStoryIdAndUserId(storyId,userId);
        storyLikeRepository.deleteById(storyLike.getId());
    }
}
