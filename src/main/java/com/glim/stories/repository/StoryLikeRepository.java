package com.glim.stories.repository;

import com.glim.stories.domain.StoryLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryLikeRepository extends JpaRepository<StoryLikes, Long> {
    boolean existsByStoryIdAndUserId(Long storyId, Long userId);
}
