package com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

import java.util.List;

@Data
@JsonTypeName("8")
public class TimelineCarouselMedia extends TimelineMedia {
    private int carousel_media_count;
    private List<CaraouselItem> carousel_media;
}
