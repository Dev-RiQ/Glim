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
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
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
    private final FFmpeg ffmpeg;
    private final FFprobe ffprobe;
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
        return new File(convertToWebp(file.getParentFile()+"/"+file.getName()));
    }

    private File setNewFile(String name, int size){
        int index = name.lastIndexOf(".");
        return new File(path+name.substring(0, index)+"_"+size+"x"+size + name.substring(index));
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        return Scalr.resize(originalImage, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
    }

    public String convertToWebp(String filename) throws Exception {
        FFmpegProbeResult ffmpegProbeResult = ffprobe.probe(filename);
        String name = filename.substring(0, filename.lastIndexOf(".")) + ".webp";
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(ffmpegProbeResult)
                .addOutput(name)
                .setFormat("webp")
                .done();

        FFmpegExecutor executable = new FFmpegExecutor(ffmpeg, ffprobe);
        FFmpegJob ffmpegjob = executable.createJob(builder);
        ffmpegjob.run();
        return name;
    }

}
