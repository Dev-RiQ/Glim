package com.glim.stories.repository;

import com.glim.stories.domain.StoryViews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryViewRepository extends JpaRepository<StoryViews, Long> {
    boolean existsByStoryIdAndUserId(Long storyId, Long userId);

    void deleteAllByStoryId(Long id);
}
