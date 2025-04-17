package com.glim.borad.repository;

import com.glim.borad.domain.Boards;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Boards, Long> {
    /*List<Boards> findAllById(Long id);*/

    @Override
    Optional<Boards> findById(Long aLong);


}
