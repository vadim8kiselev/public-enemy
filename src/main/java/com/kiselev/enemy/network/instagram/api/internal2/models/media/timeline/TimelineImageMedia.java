package com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.ImageVersions;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.ImageVersionsMeta;

import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.TimelineMedia;
import lombok.Data;

@Data
@JsonTypeName("1")
public class TimelineImageMedia extends TimelineMedia {
    private ImageVersions image_versions2;
    private List<ImageVersionsMeta> candidates;
    private long video_duration;
    private boolean has_audio;
    private int original_width;
    private int original_height;
    private int view_count;
}
