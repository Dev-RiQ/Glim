package com.glim.common.awsS3.repository;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.glim.common.awsS3.domain.AwsS3;
import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.domain.FileType;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.fileEncoder.controller.FileEncoderController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AwsS3Repository {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;
    private final FileEncoderController fileEncoderController;
    private final AwsS3Util awsS3Util;

    // MultipartFile을 전달받아 File로 전환한 후 S3에 업로드
    public List<String> upload(List<MultipartFile> multipartFiles, FileType fileType) {
        List<File> uploadFiles = fileEncoderController.fileEncoding(multipartFiles, fileType);
        return upload(uploadFiles, fileType.getType());
    }

    private List<String> upload(List<File> uploadFile, String fileType) {
        List<AwsS3> files = uploadFile.stream().map((file) -> new AwsS3(file, fileType.toLowerCase()+"/"+file.getName())).collect(Collectors.toList());
        List<String> uploadUrl = putS3(files);
        removeNewFile(fileType); // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)
        return awsS3Util.getSaveFilenames(uploadUrl); // 업로드된 파일의 S3 URL 파일 이름 반환
    }

    // 실질적인 s3 업로드 부분
    private List<String> putS3(List<AwsS3> uploadFile) {
        List<String> uploadUrl = new ArrayList<>();
        for (AwsS3 s3 : uploadFile) {
            String fileName = s3.getFilename();
            amazonS3Client.putObject(
                    new PutObjectRequest(bucket, fileName, s3.getUploadFile())
                            .withCannedAcl(CannedAccessControlList.PublicRead) // PublicRead 권한으로 업로드
            );
            uploadUrl.add(amazonS3Client.getUrl(bucket, fileName).toString());
        }
        return uploadUrl;
    }

    private void removeNewFile(String fileType) {
        File[] files = new File(System.getProperty("user.dir") + "/" + fileType).listFiles();
        if(files == null) return;
        for(File file : files) {
            if(file.delete()){
                log.info("파일이 삭제되었습니다.");
            } else {
                log.info("파일이 삭제되지 못했습니다.");
            }
        }
    }

}
