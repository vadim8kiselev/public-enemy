package com.kiselev.enemy.network.instagram.api.internal2.models.direct.item;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.thread.ThreadMedia;
import lombok.Data;

import java.util.List;

@Data
@JsonTypeName("raven_media")
public class ThreadRavenMediaItem extends ThreadItem {
    private ThreadVisualMedia visual_media;

    @Data
    public static class ThreadVisualMedia {
        private long url_expire_at_secs;
        private int playback_duration_secs;
        private ThreadMedia media;
        private List<String> seen_user_ids;
        private String view_mode;
        private int seen_count;
    }
}
