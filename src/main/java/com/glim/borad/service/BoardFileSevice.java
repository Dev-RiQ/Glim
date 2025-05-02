package com.glim.borad.service;

import com.glim.borad.domain.BoardFiles;
import com.glim.borad.domain.FileType;
import com.glim.borad.dto.request.AddBoardFileRequest;
import com.glim.borad.repository.BoardFileRepository;
import com.glim.borad.repository.BoardRepository;
import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardFileSevice {

    private final AwsS3Util awsS3Util;

    private final BoardFileRepository boardFileRepository;
    private final BoardRepository boardRepository;

//    @Transactional
//    public BoardFiles insert(AddBoardFileRequest request) {
//        return boardFileRepository.save(new AddBoardFileRequest().toEntity(request));
//    }

    @Transactional
    public void delete(Long id, Long boardId) {
        boardRepository.findById(boardId).orElseThrow(ErrorCode::throwDummyNotFound);
        boardFileRepository.deleteById(id);
    }

    public String RankImgfindById(Long boardId) {
        BoardFiles boardFiles = boardFileRepository.findById(boardId).orElseThrow(ErrorCode::throwDummyNotFound);
        return boardFiles.getFileType() == FileType.IMAGE ? awsS3Util.getURL(boardFiles.getFileName(), FileSize.IMAGE_512) :
                awsS3Util.getURL(boardFiles.getFileName(), FileSize.VIDEO_THUMBNAIL);
    }
}
