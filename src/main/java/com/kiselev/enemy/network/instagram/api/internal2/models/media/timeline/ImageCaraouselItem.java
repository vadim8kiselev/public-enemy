package com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.ImageVersions;

import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.CaraouselItem;
import lombok.Data;

// media_type 1
@Data
@JsonTypeName("1")
public class ImageCaraouselItem extends CaraouselItem {
    private ImageVersions image_versions2;
}
