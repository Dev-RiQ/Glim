package com.glim.common.fileEncoder.config;

import com.sksamuel.scrimage.AwtImage;
import com.sksamuel.scrimage.metadata.ImageMetadata;
import com.sksamuel.scrimage.nio.ImageWriter;
import com.sksamuel.scrimage.nio.PngWriter;
import com.sksamuel.scrimage.webp.CWebpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class WebpWriter implements ImageWriter {

    public static final WebpWriter DEFAULT = new WebpWriter();
    public static final WebpWriter CUSTOMWRITER = WebpWriter.DEFAULT.withQ(80).withM(6).withZ(9);

    private final CWebpHandler handler = new CWebpHandler();

    private final int z;
    private final int q;
    private final int m;
    private final boolean lossless;

    public WebpWriter() {
        z = -1;
        q = -1;
        m = -1;
        lossless = false;
    }

    public WebpWriter(int z, int q, int m, boolean lossless) {
        this.z = z;
        this.q = q;
        this.m = m;
        this.lossless = lossless;
    }

    public WebpWriter withLossless() {
        return new WebpWriter(z, q, m, true);
    }

    public WebpWriter withQ(int q) {
        if (q < 0) throw new IllegalArgumentException("q must be between 0 and 100");
        if (q > 100) throw new IllegalArgumentException("q must be between 0 and 100");
        return new WebpWriter(z, q, m, lossless);
    }

    public WebpWriter withM(int m) {
        if (m < 0) throw new IllegalArgumentException("m must be between 0 and 6");
        if (m > 6) throw new IllegalArgumentException("m must be between 0 and 6");
        return new WebpWriter(z, q, m, lossless);
    }

    public WebpWriter withZ(int z) {
        if (z < 0) throw new IllegalArgumentException("z must be between 0 and 9");
        if (z > 9) throw new IllegalArgumentException("z must be between 0 and 9");
        return new WebpWriter(z, q, m, lossless);
    }

    @Override
    public void write(AwtImage awtImage, ImageMetadata imageMetadata, OutputStream outputStream) throws IOException {
        byte[] bytes = handler.convert(awtImage.bytes(PngWriter.NoCompression), m, q, z, lossless);
        outputStream.write(bytes);
    }
}
