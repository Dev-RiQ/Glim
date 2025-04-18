package com.glim.stories.repository;

import com.glim.stories.domain.Stories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Stories, Long> {
}
