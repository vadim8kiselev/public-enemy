package com.kiselev.enemy.network.instagram.api.internal2.models.media;

import java.util.List;

import com.kiselev.enemy.network.instagram.api.internal2.models.media.ImageVersionsMeta;
import lombok.Data;

@Data
public class ImageVersions {
    private List<ImageVersionsMeta> candidates;
}
