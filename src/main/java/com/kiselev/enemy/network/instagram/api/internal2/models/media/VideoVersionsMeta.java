package com.kiselev.enemy.network.instagram.api.internal2.models.media;

import lombok.Data;

import java.net.URL;

@Data
public class VideoVersionsMeta {
    private int height;
    private int width;
    private String id;
    private String type;
    private URL url;
}
