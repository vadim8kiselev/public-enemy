package com.kiselev.enemy.network.instagram.api.internal2.models.media;

import com.kiselev.enemy.network.instagram.api.internal2.models.IGBaseModel;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.Comment.Caption;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.User;
import lombok.Data;

@Data
public class Media extends IGBaseModel {
    private String pk;
    private String id;
    private long taken_at;
    private long device_timestamp;
    private int media_type;
    private String code;
    private String client_cache_key;
    private int filter_type;
    private User user;
    private Caption caption;
    private boolean can_viewer_reshare;
    private boolean photo_of_you;
    private boolean can_viewer_save;
}
