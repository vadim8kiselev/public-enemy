package com.kiselev.enemy.network.instagram.api.internal2.responses.feed;

import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.TimelineMedia;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGPaginatedResponse;
import lombok.Data;

import java.util.List;

@Data
public class FeedUserResponse extends IGPaginatedResponse {
    private List<TimelineMedia> items;
    private String next_max_id;
    private int num_results;

    public boolean isMore_available() {
        return next_max_id != null;
    }
}
