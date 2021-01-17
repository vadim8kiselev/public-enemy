package com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.ImageVersions;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.VideoVersionsMeta;
import lombok.Data;

import java.util.List;

// media_type 2
@Data
@JsonTypeName("2")
public class TimelineVideoMedia extends TimelineMedia {
    private List<VideoVersionsMeta> video_versions;
    private ImageVersions image_versions2;
    private long video_duration;
    private boolean has_audio;
    private int original_width;
    private int original_height;
    private int view_count;
}
