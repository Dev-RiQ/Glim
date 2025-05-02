package com.glim.borad.repository;

import com.glim.borad.domain.BoardTags;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardTagRepository extends JpaRepository<BoardTags, Long> {
}
