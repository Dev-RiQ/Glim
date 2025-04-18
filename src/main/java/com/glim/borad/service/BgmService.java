package com.glim.borad.service;

import com.glim.borad.dto.request.AddBgmRequest;
import com.glim.borad.dto.response.ViewBgmResponse;
import com.glim.borad.repository.BgmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BgmService {

    private final BgmRepository bgmRepository;

    @Transactional
    public void insert(AddBgmRequest request) {
        bgmRepository.save(new AddBgmRequest().toEntity(request));
    }

    @Transactional
    public void delete(Long id) {
        bgmRepository.deleteById(id);
    }

    public List<ViewBgmResponse> list() {
        return bgmRepository.findAll().stream().map(bgm -> new ViewBgmResponse(bgm)).collect(Collectors.toList());
    }
}
