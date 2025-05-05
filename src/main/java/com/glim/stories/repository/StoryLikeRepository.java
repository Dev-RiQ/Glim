package com.glim.stories.repository;

import com.glim.stories.domain.StoryLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoryLikeRepository extends JpaRepository<StoryLikes, Long> {
    boolean existsByStoryIdAndUserId(Long storyId, Long userId);

    Optional<StoryLikes> findByStoryIdAndUserId(Long storyId, Long userId);

    void deleteAllByStoryId(Long id);
}
