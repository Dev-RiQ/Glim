package com.glim.borad.repository;

import com.glim.borad.domain.Bgms;
import com.glim.borad.domain.Boards;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BgmRepository  extends JpaRepository<Bgms, Long> {


    @Query(value = "SELECT b FROM Bgms b WHERE b.id >= :startId ORDER BY b.id ASC")
    List<Bgms> findFirst10From(@Param("startId") Long startId);

    @Query(value = "SELECT b FROM Bgms b WHERE b.id > :offset AND b.id >= :startId ORDER BY b.id ASC")
    List<Bgms> findFirst10FromOffset(@Param("startId") Long startId, @Param("offset") Long offset);
}
