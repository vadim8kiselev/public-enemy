package com.kiselev.enemy.network.instagram.api.internal2.models.direct;

import java.util.List;

import com.kiselev.enemy.network.instagram.api.internal2.models.direct.item.ThreadRavenMediaItem;

import lombok.Data;

@Data
public class DirectStory {
    private List<ThreadRavenMediaItem> items;
    private long last_activity_at;
    private boolean has_newer;
    private String next_cursor;
}
