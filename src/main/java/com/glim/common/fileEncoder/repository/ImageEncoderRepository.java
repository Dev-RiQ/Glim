package com.glim.common.fileEncoder.repository;

import com.glim.common.awsS3.domain.FileType;
import com.glim.common.awsS3.repository.AwsS3Repository;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.fileEncoder.config.FFmpegConfig;
import com.glim.common.fileEncoder.config.WebpWriter;
import com.sksamuel.scrimage.ImmutableImage;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ImageEncoderRepository {

    private final AwsS3Util awsS3Util;
    @Value("${file.path}")
    private String path;

    public List<File> userImageEncoding(List<MultipartFile> multipartFiles) throws Exception {
        List<File> files = new ArrayList<>();
        Path paths = Paths.get(path + FileType.USER_IMAGE.getType());
        Files.createDirectories(paths);
        for (MultipartFile multipartFile : multipartFiles) {
            String saveFileName = FileType.USER_IMAGE.getType() + "/" + awsS3Util.changedFileName(multipartFile.getOriginalFilename());
            int size = 128;
            files.add(getConvertImage(multipartFile, saveFileName, size));
        }
        return files;
    }

    public List<File> imageEncoding(List<MultipartFile> multipartFiles) throws Exception {
        List<File> files = new ArrayList<>();
        Path paths = Paths.get(path + FileType.IMAGE.getType());
        Files.createDirectories(paths);
        for (MultipartFile multipartFile : multipartFiles) {
            String saveFileName = FileType.IMAGE.getType() + "/" + awsS3Util.changedFileName(multipartFile.getOriginalFilename());
            for(int i = 0 ; i < 2 ; i++){
                int size = (int) (128 * Math.pow(4, i));
                files.add(getConvertImage(multipartFile, saveFileName, size));
            }
        }
        return files;
    }

    private File getConvertImage(MultipartFile multipartFile, String saveFileName, int size) throws Exception{
        BufferedImage bi = ImageIO.read(multipartFile.getInputStream());
        bi = resizeImage(bi,size,size);
        File file = setNewFile(saveFileName, size);
        ImageIO.write(bi,"png", file);
        return convertToWebp(file.getParentFile()+"/"+file.getName(), file);
    }

    private File setNewFile(String name, int size){
        int index = name.lastIndexOf(".");
        return new File(path+name.substring(0, index)+"_"+size+"x"+size + name.substring(index));
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        return Scalr.resize(originalImage, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
    }

    public File convertToWebp(String filename, File originalFile) {
        try {
            return ImmutableImage.loader()// 라이브러리 객체 생성
                    .fromFile(originalFile)
                    .output(WebpWriter.CUSTOMWRITER, new File(path+filename.substring(0, filename.lastIndexOf(".")) + ".webp")); // 손실 압축 설정, fileName.webp로 파일 생성
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNSUCCESSFUL_WEBP_CHANGE);
        }
    }

}
