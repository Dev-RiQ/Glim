package com.glim.stories.service;

import com.glim.stories.dto.request.AddStoryTagRequest;
import com.glim.stories.repository.StoryTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoryTagService {

    private final StoryTagRepository storyTagRepository;

    @Transactional
    public void insert(AddStoryTagRequest request) {
        storyTagRepository.save(new AddStoryTagRequest().toEntity(request));
    }

    @Transactional
    public void delete(Long id) {
        storyTagRepository.deleteById(id);
    }
}
