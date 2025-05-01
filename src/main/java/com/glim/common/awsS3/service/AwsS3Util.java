package com.glim.common.awsS3.service;

import com.glim.common.awsS3.domain.FileSize;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AwsS3Util {
    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private static final String URL = "https://s3.%s.amazonaws.com/%s/%s%s";

    public String getURL(String fileName, FileSize typeAndSize){
        return java.lang.String.format(URL, region, bucket, fileName, typeAndSize.getTypeAndSizeUri());
    }

    public List<String> getSaveFilenames(List<String> originalFilenames){
        Set<String> saveFilenames = new HashSet<>();
        for (String originalFilename : originalFilenames){
            String saveFilename = originalFilename.substring(originalFilename.indexOf(bucket) + bucket.length() + 1 ,originalFilename.lastIndexOf("_"));
            saveFilenames.add(saveFilename);
        }
        return saveFilenames.stream().toList();
    }

    // 랜덤 파일 이름 메서드 (파일 이름 중복 방지)
    public String changedFileName(String originName) {
        String random = UUID.randomUUID().toString();
        return random + "_" + originName;
    }
}
