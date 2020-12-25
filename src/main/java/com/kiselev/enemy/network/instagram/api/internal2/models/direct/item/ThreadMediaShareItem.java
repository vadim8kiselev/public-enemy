package com.kiselev.enemy.network.instagram.api.internal2.models.direct.item;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kiselev.enemy.network.instagram.api.internal2.models.direct.item.ThreadItem;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.Media;

import lombok.Data;

@Data
@JsonTypeName("media_share")
public class ThreadMediaShareItem extends ThreadItem {
    private Media media;
}
