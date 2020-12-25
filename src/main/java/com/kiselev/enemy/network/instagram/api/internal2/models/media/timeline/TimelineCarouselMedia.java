package com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.CaraouselItem;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.TimelineMedia;
import lombok.Data;

@Data
@JsonTypeName("8")
public class TimelineCarouselMedia extends TimelineMedia {
    private int carousel_media_count;
    private List<CaraouselItem> carousel_media;
}
