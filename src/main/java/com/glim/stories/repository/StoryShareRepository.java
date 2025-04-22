package com.glim.stories.repository;

import com.glim.stories.domain.StoryShare;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryShareRepository extends JpaRepository<StoryShare, Long> {
}
