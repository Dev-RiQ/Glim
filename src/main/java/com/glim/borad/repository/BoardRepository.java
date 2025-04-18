package com.glim.borad.repository;

import com.glim.borad.domain.Boards;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Boards, Long> {

    @Override
    Optional<Boards> findById(Long aLong);

    List<Boards> findAllByUserIdOrderByIdAsc(Long userId, Limit of);

    List<Boards> findAllByUserIdAndIdLessThanOrderByIdAsc(Long userId, Long offset, Limit of);

    List<Boards> findAllByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long offset, Limit of);

    List<Boards> findAllByUserIdOrderByIdDesc(Long userId, Limit of);

    List<Boards> findAllByUserIdAndIdGreaterThanOrderByIdAsc(Long userId, Long offset, Limit of);
}
