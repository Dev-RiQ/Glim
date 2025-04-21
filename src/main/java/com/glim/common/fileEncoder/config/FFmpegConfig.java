package com.glim.common.fileEncoder.config;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Slf4j
@Configuration
public class FFmpegConfig {

    @Value("${ffmpeg.exe.location}")
    private String ffmpegLocation;
    @Value("${ffprobe.exe.location}")
    private String ffprobeLocation;

    private final String osName = System.getProperty("os.name");

    @Bean(name = "ffmpeg")
    public FFmpeg ffMpeg() throws IOException {
        if (osName.toLowerCase().contains("win")) {
            return new FFmpeg(new ClassPathResource(ffmpegLocation).getURL().getPath());
        } else {
            return new FFmpeg(ffmpegLocation);
        }
    }

    @Bean(name = "ffprobe")
    public FFprobe ffProbe() throws IOException {
        if (osName.toLowerCase().contains("win")) {
            return new FFprobe(new ClassPathResource(ffprobeLocation).getURL().getPath());
        } else {
            return new FFprobe(ffprobeLocation);
        }
    }

    @Bean
    public FFmpegExecutor ffmpegExecutor(FFmpeg fFmpeg, FFprobe fFprobe) {
        return new FFmpegExecutor(fFmpeg, fFprobe);
    }
}
