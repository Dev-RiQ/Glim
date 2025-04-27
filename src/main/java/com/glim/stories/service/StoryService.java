package com.glim.stories.service;

import com.glim.common.exception.ErrorCode;
import com.glim.stories.domain.Stories;
import com.glim.stories.dto.request.AddStoryRequest;
import com.glim.stories.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoryService {

    private final StoryRepository storyRepository;

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

    @Transactional
    public void deleteStoriesByUser(Long userId) {
        storyRepository.deleteByUserId(userId);
    }




}
