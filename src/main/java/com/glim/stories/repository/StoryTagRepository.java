package com.glim.stories.repository;

import com.glim.stories.domain.StoryTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryTagRepository extends JpaRepository<StoryTag, Long> {
}
