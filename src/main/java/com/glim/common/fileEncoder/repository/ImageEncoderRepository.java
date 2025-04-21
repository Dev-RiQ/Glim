package com.glim.common.fileEncoder.repository;

import com.glim.common.awsS3.domain.FileType;
import com.glim.common.awsS3.repository.AwsS3Repository;
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
public class ImageEncoderRepository {

    private final AwsS3Util awsS3Util;

    public List<File> imageEncoding(List<MultipartFile> multipartFiles) throws Exception {
        List<File> files = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String saveFileName = FileType.IMAGE.getType() + "/" + awsS3Util.changedFileName(multipartFile.getOriginalFilename());
            for(int i = 0 ; i < 4 ; i++){
                BufferedImage bi = ImageIO.read(multipartFile.getInputStream());
                int size = (int) (128 * Math.pow(2, i));
                bi = resizeImage(bi,size,size);
                File file = setNewFile(saveFileName, size);
                ImageIO.write(bi,"jpg", file);
                files.add(convertToWebp(file.getParentFile()+"/"+file.getName(), file));
            }
        }
        return files;
    }

    private File setNewFile(String name, int size){
        int index = name.lastIndexOf(".");
        return new File(name.substring(0, index)+"_"+size+"x"+size + name.substring(index));
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        return Scalr.resize(originalImage, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
    }

    public File convertToWebp(String filename, File originalFile) {
        try {
            return ImmutableImage.loader()// 라이브러리 객체 생성
                    .fromFile(originalFile)
                    .output(WebpWriter.CUSTOMWRITER, new File(filename.substring(0, filename.lastIndexOf(".")) + ".webp")); // 손실 압축 설정, fileName.webp로 파일 생성
        } catch (Exception e) {
            throw new CustomException(ErrorCode.DUMMY_BAD_REQUEST);
        }
    }

}
