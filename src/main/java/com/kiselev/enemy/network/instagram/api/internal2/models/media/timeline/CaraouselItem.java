package com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGBaseModel;

import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.ImageCaraouselItem;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.VideoCaraouselItem;
import lombok.Data;

@Data
@JsonTypeInfo(defaultImpl = CaraouselItem.class, use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "media_type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ImageCaraouselItem.class),
        @JsonSubTypes.Type(value = VideoCaraouselItem.class)
})
public class CaraouselItem extends IGBaseModel {
    private int original_width;
    private int original_height;
    private int media_type;
    private String carousel_parent_id;
}
