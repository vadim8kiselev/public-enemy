package com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.ImageVersions;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.VideoVersionsMeta;
import lombok.Data;

import java.util.List;

@Data
@JsonTypeName("2")
public class VideoCaraouselItem extends CaraouselItem {
    private ImageVersions image_versions2;
    private List<VideoVersionsMeta> video_versions;
}
