package com.glim.common.fileEncoder.repository;

import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.fileEncoder.config.WebpWriter;
import com.sksamuel.scrimage.ImmutableImage;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AudioEncoderRepository {

    private final AwsS3Util awsS3Util;

    public List<File> audioEncoding(List<MultipartFile> multipartFiles) throws Exception {
        return null;
    }
}
