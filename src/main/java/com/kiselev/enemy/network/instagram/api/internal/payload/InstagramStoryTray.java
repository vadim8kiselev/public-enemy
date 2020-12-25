package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * StoryTray
 *
 * @author Ozan Karaali
 */

@Getter
@Setter
@ToString(callSuper = true)
public class InstagramStoryTray extends StatusResult {
    private String id;
    private List<InstagramStoryItem> items; //Item[]
    private InstagramUser user;
    private boolean can_reply;
    private long expiring_at;
    private int seen_ranked_position;
    private int seen;
    private String latest_reel_media;
    private int ranked_position;
    private boolean is_nux;
    private String show_nux_tooltip;
    private boolean muted;
    private int prefetch_count;
    private String location; //Location
    private String owner; //Owner
    private String nux_id;
    private String dismiss_card; //DismissCard
    private boolean can_reshare;
    private boolean has_besties_media;
    private String reel_type;
}