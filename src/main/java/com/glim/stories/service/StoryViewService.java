package com.glim.stories.service;

import com.glim.stories.dto.request.AddStoryViewRequest;
import com.glim.stories.repository.StoryViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoryViewService {

    private final StoryViewRepository storyViewRepository;

    @Transactional
    public void insert(AddStoryViewRequest request) {
        storyViewRepository.save(new AddStoryViewRequest().toEntity(request));
    }
}
