package com.glim.admin.service;

import com.glim.admin.domain.Advertisement;
import com.glim.admin.domain.AdvertisementStatus;
import com.glim.admin.repository.AdvertisementRepository;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    public List<Advertisement> findAll() {
        List<Advertisement> list = advertisementRepository.findAll();
        if(list.isEmpty()) throw new CustomException(ErrorCode.ADVERTISEMENT_NOT_FOUND);
        return list;
    }

    public Advertisement findById(Long id) {
        return advertisementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("광고를 찾을 수 없습니다. id=" + id));
    }

    @Transactional
    public Advertisement create(Long boardId) {
        return advertisementRepository.save(new Advertisement(boardId));
    }

    @Transactional
    public Advertisement approve(Long id) {
        Advertisement advertisement = findById(id);
        advertisement.setStatus(AdvertisementStatus.APPROVED);
        advertisement.setRejectionReason("");
        return advertisement;
    }

    @Transactional
    public Advertisement reject(Long id) {
        Advertisement advertisement = findById(id);
        advertisement.setStatus(AdvertisementStatus.REJECTED);
        advertisement.setRejectionReason("");
        return advertisement;
    }
    @Transactional

    public void delete(Long id) {
        advertisementRepository.deleteById(id);
    }

    public List<Advertisement> findByStatus() {
        return advertisementRepository.findByStatus(AdvertisementStatus.APPROVED);
    }
}
