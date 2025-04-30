package com.glim.stories.service;

import com.glim.common.exception.ErrorCode;
import com.glim.stories.domain.Stories;
import com.glim.stories.dto.request.AddStoryRequest;
import com.glim.stories.dto.response.ViewStoryResponse;
import com.glim.stories.repository.StoryLikeRepository;
import com.glim.stories.repository.StoryRepository;
import com.glim.stories.repository.StoryTagRepository;
import com.glim.stories.repository.StoryViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoryService {

    private final StoryRepository storyRepository;
    private final StoryLikeRepository storyLikeRepository;
    private final StoryViewRepository storyViewRepository;

    @Transactional
    public void insert(AddStoryRequest request) {
        storyRepository.save(new AddStoryRequest().toEntity(request));
    }

    @Transactional
    public void delete(Long id) {
        storyRepository.deleteById(id);
    }

    @Transactional
    public Stories updateLike(Long id, int like) {
        Stories stories = storyRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        stories.setLikes(stories.getLikes() + like);
        storyRepository.save(stories);
        return stories;
    }

    @Transactional
    public Stories updateView(Long id, int view) {
        Stories stories = storyRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        stories.setViews(stories.getViews() + view);
        storyRepository.save(stories);
        return stories;
    }

    public Boolean isStory(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusHours(24);

        return storyRepository.existsByUserIdAndCreatedAtBetween(userId, yesterday, now);
    }

    @Transactional
    public void deleteStoriesByUser(Long userId) {
        storyRepository.deleteByUserId(userId);
    }


    public Stories getStory(Long storyId) {
        return storyRepository.findById(storyId).orElseThrow(ErrorCode::throwDummyNotFound);
    }

    public List<ViewStoryResponse> getStoryList(Long id) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusHours(24);
        List<Stories> stories = storyRepository.findByUserIdAndCreatedAtBetween(id, yesterday, now);
        List<ViewStoryResponse> responses = stories.stream().map(ViewStoryResponse::new).collect(Collectors.toList());
        responses.forEach(viewStoryResponse -> {
            boolean isLike = storyLikeRepository.existsByStoryIdAndUserId(viewStoryResponse.getStoryId(), viewStoryResponse.getUserId());
            boolean isView = storyViewRepository.existsByStoryIdAndUserId(viewStoryResponse.getStoryId(), viewStoryResponse.getUserId());
            viewStoryResponse.setLike(isLike);
            viewStoryResponse.setViewed(isView);
        });
        return responses;
    }
}


