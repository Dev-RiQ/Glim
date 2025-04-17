package com.glim.borad.repository;

import com.glim.borad.domain.BoardViews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardViewRepository extends JpaRepository<BoardViews, Long> {
}
