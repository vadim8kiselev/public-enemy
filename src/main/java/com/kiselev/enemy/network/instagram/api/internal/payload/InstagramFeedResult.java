package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class InstagramFeedResult extends StatusResult {

    private boolean auto_load_more_enabled;
    private int num_results;
    private String next_max_id;

    private List<InstagramFeedItem> items;
    private List<InstagramFeedItem> ranked_items;

    private boolean more_available;

}
