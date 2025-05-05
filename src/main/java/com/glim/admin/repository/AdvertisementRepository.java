package com.glim.admin.repository;

import com.glim.admin.domain.Advertisement;
import com.glim.admin.domain.AdvertisementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByStatus(AdvertisementStatus advertisementStatus);
    @Query(value = "SELECT * FROM advertisement WHERE status = 'APPROVED' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Advertisement findRandomApprovedAdvertisement();
}
