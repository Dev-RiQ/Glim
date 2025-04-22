package com.glim.stories.service;

import com.glim.borad.dto.request.AddBoardShareRequest;
import com.glim.borad.repository.BoardShareRepository;
import com.glim.stories.dto.request.AddStoryShareRequest;
import com.glim.stories.repository.StoryShareRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoryShareService {

    private final StoryShareRepository storyShareRepository;

    @Transactional
    public void insert(AddStoryShareRequest request) {
        storyShareRepository.save(new AddStoryShareRequest().toEntity(request));
    }

    @Transactional
    public void delete(Long id) {
        storyShareRepository.deleteById(id);
    }


}
