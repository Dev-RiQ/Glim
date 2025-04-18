package com.glim.common.fileEncoder.controller;

import com.glim.common.awsS3.domain.FileType;
import com.glim.common.fileEncoder.service.FileEncoderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FileEncoderController {

    private final FileEncoderService fileEncoderService;

    public List<File> fileEncoding(List<MultipartFile> multipartFiles, FileType type) {
        return fileEncoderService.changeEncoding(multipartFiles, type);
    }
}
