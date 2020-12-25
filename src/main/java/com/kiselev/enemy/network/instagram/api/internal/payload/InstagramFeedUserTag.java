package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Data;

@Data
public class InstagramFeedUserTag {

    public InstagramUserSummary user;
    private float[] position = {0, 0};
    private float start_time_in_video_in_sec;
    private float duration_in_video_in_sec;

}
