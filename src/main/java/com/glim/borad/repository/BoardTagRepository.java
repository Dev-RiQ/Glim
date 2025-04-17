package com.glim.borad.repository;

import com.glim.borad.domain.BoardTags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardTagRepository extends JpaRepository<BoardTags, Long> {
}
