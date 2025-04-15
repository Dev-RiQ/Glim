package com.glim.dummy.service;

import com.glim.common.exception.ErrorCode;
import com.glim.dummy.domain.Dummy;
import com.glim.dummy.dto.request.AddDummyRequest;
import com.glim.dummy.dto.request.UpdateDummyRequest;
import com.glim.dummy.dto.response.ViewDummyResponse;
import com.glim.dummy.repository.DummyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DummyService {

    private final DummyRepository dummyRepository;

    public ViewDummyResponse findDummyById(Long id) {
        return new ViewDummyResponse(dummyRepository.findById(id).orElseThrow(ErrorCode::throwUserNotFound));
    }

    @Transactional
    public void insert(AddDummyRequest request) {
        dummyRepository.save(new AddDummyRequest().toEntity(request));
    }

    @Transactional
    public void update(Long id, UpdateDummyRequest request) {
        Dummy dummy = dummyRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        dummy.update(request);
        dummyRepository.save(dummy);
    }

    @Transactional
    public void delete(Long id) {
        Dummy dummy = dummyRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        dummyRepository.delete(dummy);
    }
}
