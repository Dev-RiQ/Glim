package com.glim.common.awsS3.controller;

import com.glim.common.awsS3.domain.FileType;
import com.glim.common.awsS3.service.AwsS3Service;
import com.glim.common.statusResponse.StatusResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class AwsS3Controller {

    private final AwsS3Service awsS3Service;

    @PostMapping("")
    public StatusResponseDTO uploadFile(@RequestParam("images") List<MultipartFile> multipartFiles, String fileType) {
        FileType type = FileType.valueOf(fileType);
        List<String> list = awsS3Service.saveFile(multipartFiles, type);
        list.forEach(System.out::println);
        return StatusResponseDTO.ok();
    }
}
