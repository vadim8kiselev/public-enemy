package com.kiselev.enemy.utils.image;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

@Data
@Accessors(fluent = true)
public class Media {

    private URL url;

    private byte[] bytes;

    private BufferedImage image;

    private List<Color> colors;

    public static Media of(URL url) {
        Media media = new Media();
        media.url = url;
        return media;
    }

    @SneakyThrows
    public byte[] bytes() {
        if (this.bytes == null) {
            this.bytes = IOUtils.toByteArray(url);
        }
        return this.bytes;
    }

    @SneakyThrows
    public BufferedImage image() {
        if (this.image == null) {
            final URLConnection urlConnection = url.openConnection();
            try {
                try (InputStream inputStream = urlConnection.getInputStream()) {
                    this.image = ImageIO.read(inputStream);
                }
            } finally {
                IOUtils.close(urlConnection);
            }
        }
        return this.image;
    }

    public List<Color> colors() {
        if (this.colors == null) {
            this.colors = Lists.newArrayList();
            BufferedImage image = image();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    int rgb = image.getRGB(x, y);
                    this.colors.add(Color.of(rgb));
                }
            }
        }
        return this.colors;
    }
}
