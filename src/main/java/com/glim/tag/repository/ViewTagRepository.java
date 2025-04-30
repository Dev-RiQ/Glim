package com.glim.tag.repository;

import com.glim.tag.domain.ViewTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ViewTagRepository extends JpaRepository<ViewTag, Long> {

    Optional<ViewTag> findByUserIdAndTag(Long userId, String tag);

    List<ViewTag> findTop20ByUserIdOrderByViewsDesc(Long userId);


}
