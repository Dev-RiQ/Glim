package com.glim.admin.service;

import com.glim.admin.domain.Advertisement;
import com.glim.admin.domain.AdvertisementStatus;
import com.glim.admin.repository.AdvertisementRepository;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
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

    public Advertisement create(Advertisement advertisement) {
        advertisement.setStatus(AdvertisementStatus.PENDING);
        advertisement.setRejectionReason(""); // 기본값은 빈 문자열
        return advertisementRepository.save(advertisement);
    }

    public Advertisement approve(Long id) {
        Advertisement advertisement = findById(id);
        advertisement.setStatus(AdvertisementStatus.APPROVED);
        advertisement.setRejectionReason(""); // 승인 시 거절 사유는 비워줍니다
        return advertisementRepository.save(advertisement);
    }

    public Advertisement reject(Long id, String reason) {
        Advertisement advertisement = findById(id);
        advertisement.setStatus(AdvertisementStatus.REJECTED);
        advertisement.setRejectionReason(reason);
        return advertisementRepository.save(advertisement);
    }

    public void delete(Long id) {
        advertisementRepository.deleteById(id);
    }

    public List<Advertisement> findByStatus(AdvertisementStatus advertisementStatus) {
        List<Advertisement> list = advertisementRepository.findByStatus(AdvertisementStatus.APPROVED);
        return list;
    }
}
