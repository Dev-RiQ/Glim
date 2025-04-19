package com.glim.common.fileEncoder.repository;

import com.glim.common.awsS3.domain.FileType;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.fileEncoder.config.WebpWriter;
import com.sksamuel.scrimage.ImmutableImage;
import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AudioEncoderRepository {

    private final FFmpeg ffmpeg;
    private final FFprobe ffprobe;
    private final AwsS3Util awsS3Util;
    private final String path = System.getProperty("user.dir");

    public List<File> audioEncoding(List<MultipartFile> multipartFiles) throws Exception {
        List<File> files = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String saveFileName = FileType.AUDIO.getType() + "/" + awsS3Util.changedFileName(multipartFile.getOriginalFilename());
            File file = new File(saveFileName);
            Path filePath = file.toPath();
            multipartFile.transferTo(filePath);
            FFmpegProbeResult ffmpegProbeResult = ffprobe.probe(path +"\\"+ saveFileName);
            files.add(new File(convertAudio(ffmpegProbeResult, saveFileName)));
        }
        return files;
    }

    public String convertAudio(FFmpegProbeResult probeResult, String saveFileName) {
        String filename = saveFileName.substring(0, saveFileName.lastIndexOf(".")) + "_encoded.mp3";
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(probeResult)
                .addOutput(path + "\\" + filename.replace("/","\\"))
//                .addExtraArgs("-ss","00:01:00") // 시작시간
//                .addExtraArgs("-to","00:01:30") // 종료시간
                .setAudioBitRate(128000)
                .setAudioChannels(2)
                .setAudioSampleRate(44100)
                .setFormat("mp3")
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                .done();
        FFmpegExecutor executable = new FFmpegExecutor(ffmpeg, ffprobe);
        executable.createJob(builder).run();

        return filename;
    }

}
