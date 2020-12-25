package com.kiselev.enemy.network.instagram.api.internal2.models.media.reel;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.Media;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.Viewer;

import com.kiselev.enemy.network.instagram.api.internal2.models.media.reel.ReelImageMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.reel.ReelVideoMedia;
import lombok.Data;

@Data
@JsonTypeInfo(defaultImpl = ReelMedia.class, use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "media_type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ReelImageMedia.class),
        @JsonSubTypes.Type(value = ReelVideoMedia.class)
})
public class ReelMedia extends Media {
    private int viewer_count;
    private int total_viewer_count;
    private List<Viewer> viewers;
}
