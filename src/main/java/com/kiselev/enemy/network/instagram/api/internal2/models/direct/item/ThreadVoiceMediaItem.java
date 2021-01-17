package com.kiselev.enemy.network.instagram.api.internal2.models.direct.item;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.VoiceMedia;
import lombok.Data;

@Data
@JsonTypeName("voice_media")
public class ThreadVoiceMediaItem extends ThreadItem {
    private VoiceMedia media;
}
