package com.glim.stories.repository;

import com.glim.stories.domain.Stories;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StoryRepository extends JpaRepository<Stories, Long> {
    void deleteByUserId(Long userId);
    Boolean existsByUserIdAndCreatedAtBetween(Long userId, LocalDateTime createdAtAfter, LocalDateTime createdAtBefore);
    List<Stories> findByUserIdAndCreatedAtBetween(Long id, LocalDateTime yesterday, LocalDateTime now);
    Optional<List<Stories>> findAllByUserIdOrderByIdDesc(Long userId, Limit of);
    Optional<List<Stories>> findAllByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long offset, Limit of);
}
