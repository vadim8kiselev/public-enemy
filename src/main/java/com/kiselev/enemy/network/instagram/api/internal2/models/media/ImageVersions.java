package com.kiselev.enemy.network.instagram.api.internal2.models.media;

import lombok.Data;

import java.util.List;

@Data
public class ImageVersions {
    private List<ImageVersionsMeta> candidates;
}
