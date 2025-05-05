package com.glim.common.fileEncoder.service;

import com.glim.common.awsS3.domain.FileType;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.fileEncoder.repository.AudioEncoderRepository;
import com.glim.common.fileEncoder.repository.ImageEncoderRepository;
import com.glim.common.fileEncoder.repository.VideoEncoderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileEncoderService {

    private final ImageEncoderRepository imageEncoderRepository;
    private final VideoEncoderRepository videoEncoderRepository;
    private final AudioEncoderRepository audioEncoderRepository;

    public List<File> changeEncoding(List<MultipartFile> multipartFiles, FileType type) {
        List<File> files = null;
        try{
            switch (type) {
                case USER_IMAGE -> files = imageEncoderRepository.userImageEncoding(multipartFiles);
                case IMAGE -> files = imageEncoderRepository.imageEncoding(multipartFiles);
                case VIDEO -> files = videoEncoderRepository.videoEncoding(multipartFiles);
                case AUDIO -> files = audioEncoderRepository.audioEncoding(multipartFiles);
            }
        }catch (Exception e){
            throw new CustomException(ErrorCode.UNSUCCESSFUL_FILE_UPLOAD);
        }
        return files;
    }

}
