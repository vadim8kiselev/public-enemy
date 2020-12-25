package com.kiselev.enemy.network.instagram.api.internal2.models.media;

import lombok.Data;

import java.net.URL;

@Data
public class ImageVersionsMeta {
    private URL url;
    private int width;
    private int height;
}
