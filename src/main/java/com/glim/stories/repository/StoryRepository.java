package com.glim.stories.repository;

import com.glim.stories.domain.Stories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface StoryRepository extends JpaRepository<Stories, Long> {
    void deleteByUserId(Long userId);

    Optional<Stories> findByUserId(Long userId);

    Boolean existsByUserIdAndCreatedAtBetween(Long userId, LocalDateTime createdAtAfter, LocalDateTime createdAtBefore);
}
