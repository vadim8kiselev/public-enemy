package com.kiselev.enemy.network.instagram.api.internal2.models.media;

import com.kiselev.enemy.network.instagram.api.internal2.models.media.Audio;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.Media;
import lombok.Data;

@Data
// media type 11
public class VoiceMedia extends Media {
    private Audio audio;
    private String product_type;
    private String seen_user_ids;
    private String view_mode;
    private int seen_count;
    private long replay_expiring_at;
}
