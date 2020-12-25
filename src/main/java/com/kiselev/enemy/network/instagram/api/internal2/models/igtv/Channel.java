package com.kiselev.enemy.network.instagram.api.internal2.models.igtv;

import java.util.List;

import com.kiselev.enemy.network.instagram.api.internal2.models.IGBaseModel;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.TimelineVideoMedia;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.User;

import lombok.Data;

@Data
public class Channel extends IGBaseModel {
    private String id;
    private List<TimelineVideoMedia> items;
    private boolean more_available;
    private String title;
    private String type;
    private String max_id;
    private User user_dict;
    private String description;
    private String cover_photo_url;
}
