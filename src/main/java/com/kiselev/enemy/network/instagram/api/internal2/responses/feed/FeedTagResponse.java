package com.kiselev.enemy.network.instagram.api.internal2.responses.feed;

import java.util.List;

import com.kiselev.enemy.network.instagram.api.internal2.models.feed.Reel;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.timeline.TimelineMedia;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGPaginatedResponse;

import lombok.Data;

@Data
public class FeedTagResponse extends IGPaginatedResponse {
    private List<TimelineMedia> ranked_items;
    private List<TimelineMedia> items;
    private Reel story;
    private int num_results;
    private String next_max_id;
    private boolean more_available;
}
