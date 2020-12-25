package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class InstagramBroadcast extends StatusResult {
    private InstagramUser broacast_owner;
    private String broadcast_status;
    private String cover_frame_url;
    private String published_time;
    private String broadcast_message;
    private boolean muted;
    private String media_id;
    private String id;
    private String rtmp_playback_url;
    private String dash_abr_playback_url;
    private String dash_playback_url;
    private int ranked_position;
    private String organic_tracking_token;
    private int seen_ranked_position;
    private int viewer_count;
    private String dash_manifest;
    private String expire_at;
    private String encoding_tag;
    private int total_unique_viewer_count;
    private boolean internal_only;
    private int number_of_qualities;
}
