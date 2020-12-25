package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class InstagramGetMediaInfoResult extends StatusResult {
    private boolean auto_load_more_enabled;
    private int num_results;
    private boolean more_available;

    private List<InstagramFeedItem> items;


}