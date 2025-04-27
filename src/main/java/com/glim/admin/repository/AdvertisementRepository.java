package com.glim.admin.repository;

import com.glim.admin.domain.Advertisement;
import com.glim.admin.domain.AdvertisementStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByStatus(AdvertisementStatus advertisementStatus);
}
