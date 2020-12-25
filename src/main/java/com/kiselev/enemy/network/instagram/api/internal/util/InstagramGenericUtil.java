package com.kiselev.enemy.network.instagram.api.internal.util;

import lombok.extern.log4j.Log4j;
import org.apache.http.Header;
import org.apache.http.HttpMessage;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

@Log4j
public class InstagramGenericUtil {

    /**
     * Generate UUID
     *
     * @param dash If needs to keep dash
     * @return UUID
     */
    public static String generateUuid(boolean dash) {
        String uuid = UUID.randomUUID().toString();

        if (dash) {
            return uuid;
        }

        return uuid.replaceAll("-", "");
    }

    /**
     * Gets image dimensions for given file
     *
     * @param imgFile image file
     * @return dimensions of image
     * @throws IOException if the file is not a known image
     */
    public static Dimension getImageDimension(File imgFile) throws IOException {
        int pos = imgFile.getName().lastIndexOf(".");
        if (pos == -1)
            throw new IOException("No extension for file: " + imgFile.getAbsolutePath());
        String suffix = imgFile.getName().substring(pos + 1);
        Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
        while (iter.hasNext()) {
            ImageReader reader = iter.next();
            try {
                ImageInputStream stream = new FileImageInputStream(imgFile);
                reader.setInput(stream);
                int width = reader.getWidth(reader.getMinIndex());
                int height = reader.getHeight(reader.getMinIndex());
                return new Dimension(width, height);
            } catch (IOException e) {
                log.warn("Error reading: " + imgFile.getAbsolutePath(), e);
            } finally {
                reader.dispose();
            }
        }

        throw new IOException("Not a known image file: " + imgFile.getAbsolutePath());
    }

    public static boolean isImageFile(Path path) throws IOException {
        String mimeType = Files.probeContentType(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    public static boolean isVideoFile(Path path) throws IOException {
        String mimeType = Files.probeContentType(path);
        return mimeType != null && mimeType.startsWith("video");
    }

    public static Optional<String> getFirstHeaderValue(HttpMessage req, String name) {
        Header[] header = req.getHeaders(name);

        if (header.length > 0) {
            return Optional.of(header[0].getValue());
        }

        return Optional.empty();
    }
}
