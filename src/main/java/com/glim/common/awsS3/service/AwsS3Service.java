package com.glim.common.awsS3.service;

import com.glim.common.awsS3.domain.FileType;
import com.glim.common.awsS3.repository.AwsS3Repository;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
@Service
public class AwsS3Service {
    private final AwsS3Repository awsS3Repository;

    public List<String> saveFile(List<MultipartFile> multipartFiles, FileType fileType) {
        if (!multipartFiles.isEmpty()) {
            return awsS3Repository.upload(multipartFiles, fileType); // s3 버킷에 images 디렉토리에 업로드
        }else{
            log.error("no multipartFile");
            throw new CustomException(ErrorCode.DUMMY_BAD_REQUEST);
        }
    }

    public void deleteFile(String filename, FileType fileType) {
        awsS3Repository.delete(filename, fileType);
    }
}
