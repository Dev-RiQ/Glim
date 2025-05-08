package com.glim.common.fileEncoder.repository;

import com.glim.common.awsS3.domain.FileType;
import com.glim.common.awsS3.service.AwsS3Util;
import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class VideoEncoderRepository {

    private final FFmpeg ffmpeg;
    private final FFprobe ffprobe;
    private final AwsS3Util awsS3Util;
    private final ImageEncoderRepository imageEncoderRepository;
    @Value("${file.path}")
    private String path;

    public List<File> videoEncoding(List<MultipartFile> multipartFiles) throws Exception {
        List<File> files = new ArrayList<>();
        Path paths = Paths.get(path + FileType.VIDEO.getType());
        Files.createDirectories(paths);
        for (MultipartFile multipartFile : multipartFiles) {
            String saveFileName = FileType.VIDEO.getType() + "/" + awsS3Util.changedFileName(multipartFile.getOriginalFilename());
            File file = new File(path + saveFileName);
            Path filePath = file.toPath();
            multipartFile.transferTo(filePath);
            FFmpegProbeResult ffmpegProbeResult = ffprobe.probe(path + saveFileName);
            files.add(new File(convertVideo(ffmpegProbeResult, saveFileName)));
            String thumbnailName = getThumbnail(ffmpegProbeResult, saveFileName);
            files.add(imageEncoderRepository.convertToWebp(thumbnailName,new File(path + thumbnailName)));
        }
        return files;
    }

    public String convertVideo(FFmpegProbeResult probeResult, String saveFileName) {
        FFmpegStream file = probeResult.getStreams().get(0);
        String filename = saveFileName.substring(0, saveFileName.lastIndexOf(".")) + "_encoded.mp4";
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(probeResult)
                .addOutput(path + filename.replace("/","\\"))
                .setAudioCodec("aac")
                .setAudioBitRate(128000)
                .setAudioChannels(2)
                .setAudioSampleRate(44100)
                .setFormat("mp4")
                .setVideoCodec("h264")
                .setVideoBitRate(2500000)
                .setVideoFrameRate(30)
                .setVideoResolution(file.width, file.height)
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                .done();

        FFmpegExecutor executable = new FFmpegExecutor(ffmpeg, ffprobe);
        executable.createJob(builder).run();

        return filename;
    }

    private String getThumbnail(FFmpegProbeResult probeResult, String saveFileName) {
        FFmpegStream file = probeResult.getStreams().get(0);
        String filename = saveFileName.substring(0, saveFileName.lastIndexOf(".")) + "_thumbnail.jpg";
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(probeResult)
                .addOutput(path + filename.replace("/","\\"))
                .addExtraArgs("-ss","00:00:00")
                .addExtraArgs("-vframes","1")
                .setVideoResolution(file.width, file.height)
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                .done();

        FFmpegExecutor executable = new FFmpegExecutor(ffmpeg, ffprobe);
        executable.createJob(builder).run();

        return filename;
    }
}
