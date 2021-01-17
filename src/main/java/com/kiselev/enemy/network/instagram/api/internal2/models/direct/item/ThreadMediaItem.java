package com.kiselev.enemy.network.instagram.api.internal2.models.direct.item;

import com.kiselev.enemy.network.instagram.api.internal2.models.media.thread.ThreadMedia;
import lombok.Data;

@Data
public class ThreadMediaItem extends ThreadItem {
    private ThreadMedia media;
}
