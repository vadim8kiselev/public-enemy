package com.kiselev.enemy.network.instagram.api.internal2.models.direct.item;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.thread.ThreadAnimatedMedia;
import lombok.Data;

@Data
@JsonTypeName("animated_media")
public class ThreadAnimatedMediaItem extends ThreadItem {
    private ThreadAnimatedMedia media;
}
