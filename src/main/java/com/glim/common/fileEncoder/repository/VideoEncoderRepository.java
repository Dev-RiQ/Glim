package com.glim.common.fileEncoder.repository;

import com.glim.common.awsS3.domain.FileType;
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
import net.bramp.ffmpeg.builder.FFmpegOutputBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.options.AudioEncodingOptions;
import net.bramp.ffmpeg.options.EncodingOptions;
import net.bramp.ffmpeg.options.MainEncodingOptions;
import net.bramp.ffmpeg.options.VideoEncodingOptions;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.beans.Encoder;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class VideoEncoderRepository {

    private final FFmpeg ffmpeg;
    private final FFprobe ffprobe;
    private final AwsS3Util awsS3Util;
    private final String path = System.getProperty("user.dir");

    public List<File> videoEncoding(List<MultipartFile> multipartFiles) throws Exception {
        List<File> files = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String saveFileName = FileType.VIDEO.getType() + "/" + awsS3Util.changedFileName(multipartFile.getOriginalFilename());
            File file = new File(saveFileName);
            Path filePath = file.toPath();
            multipartFile.transferTo(filePath);
            FFmpegProbeResult ffmpegProbeResult = ffprobe.probe(path +"\\"+ saveFileName);
            convertVideo(ffmpegProbeResult, saveFileName);
            files.add(file);
        }
        return files;
    }

    public void convertVideo(FFmpegProbeResult probeResult, String saveFileName) {
        FFmpegStream file = probeResult.getStreams().get(0);
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(probeResult)
                .addOutput(path + "\\" + saveFileName.substring(0, saveFileName.lastIndexOf(".")).replace("/","\\") + "_encoded.mp4")
                .setAudioCodec("aac")
                .setAudioBitRate(128000)
                .setAudioChannels(2)
                .setAudioSampleRate(44100)
                .setFormat("mp4")
                .setVideoCodec("h264")
                .setVideoBitRate(320000)
                .setVideoFrameRate(30)
                .setVideoResolution(file.width, file.height)
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                .done();

        FFmpegExecutor executable = new FFmpegExecutor(ffmpeg, ffprobe);
        executable.createJob(builder).run();
    }

}
