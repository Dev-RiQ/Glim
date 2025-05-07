package com.glim.borad.service;

import com.glim.borad.domain.Bgms;
import com.glim.borad.domain.Boards;
import com.glim.borad.dto.request.AddBgmRequest;
import com.glim.borad.dto.response.ViewBgmResponse;
import com.glim.borad.repository.BgmRepository;
import com.glim.borad.repository.BoardRepository;
import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
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
    private final AwsS3Util awsS3Util;
    private final BoardRepository boardRepository;

    @Transactional
    public void insert(AddBgmRequest request) {
        bgmRepository.save(new AddBgmRequest().toEntity(request));
    }

    @Transactional
    public void delete(Long id) {
        List<Boards> boardList = boardRepository.findAllByBgmId(id);
        for (Boards board : boardList) {
            board.setBgmId(0L);
            boardRepository.save(board);
        }
        bgmRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.BGM_NOT_FOUND));
        bgmRepository.deleteById(id);
    }

    public List<ViewBgmResponse> list() {
        return bgmRepository.findAll().stream().map(this::getURL).collect(Collectors.toList());
    }

    private ViewBgmResponse getURL(Bgms bgms) {
        return new ViewBgmResponse(bgms, awsS3Util.getURL(bgms.getFileName(), FileSize.AUDIO));
    }

    public List<ViewBgmResponse> list(Long id, Long offset) {
        List<Bgms> bgmsList = offset == null ?
                bgmRepository.findFirst10From(id) :
                bgmRepository.findFirst10FromOffset(id, offset);
        return bgmsList.stream().map(this::getURL).collect(Collectors.toList());
    }
}
