package com.glim.tag.service;

import com.glim.tag.domain.ViewTag;
import com.glim.tag.repository.ViewTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewTagService {

    private final ViewTagRepository viewTagRepository;

    public void saveAllViews(Long userId, List<String> tags) {
        for (String tag : tags) {
            ViewTag viewTag = viewTagRepository.findByUserIdAndTag(userId, tag)
                .map(v -> {
                    v.increaseViews();
                    return v;
                })
                .orElse(ViewTag.builder()
                    .userId(userId)
                    .tag(tag)
                    .views(1)
                    .build());
            viewTagRepository.save(viewTag);
        }
    }

    public List<String> getTopTagsByUser(Long userId) {
        return viewTagRepository.findTop20ByUserIdOrderByViewsDesc(userId).stream()
                .map(ViewTag::getTag)
                .toList();
    }

    public List<ViewTag> getTop20ViewTags(Long userId) {
        return viewTagRepository.findTop20ByUserIdOrderByViewsDesc(userId);
    }
}
