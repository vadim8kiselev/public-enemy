package com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.kiselev.enemy.network.instagram.api.internal2.models.location.Location;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.Media;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.UserTags;

import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.Comment;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.TimelineCarouselMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.TimelineImageMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.TimelineVideoMedia;
import lombok.Data;

@Data
@JsonTypeInfo(defaultImpl = Media.class, use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "media_type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TimelineImageMedia.class),
        @JsonSubTypes.Type(value = TimelineVideoMedia.class),
        @JsonSubTypes.Type(value = TimelineCarouselMedia.class)
})
public class TimelineMedia extends Media {
    private List<Comment> preview_comments;
    private boolean has_liked;
    private int like_count;
    private int comment_count;
    private Location location;
    private UserTags usertags;
}
