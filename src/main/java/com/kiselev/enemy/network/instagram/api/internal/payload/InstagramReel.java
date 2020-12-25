package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class InstagramReel extends StatusResult {
    private String id;
    private List<InstagramStoryItem> items; //item
    private InstagramUser user;
    private long expiring_at;
    private int seen; //boolean?
    private boolean can_reply; //boolean
    private String location;
    private String latest_reel_media;
    private int prefetch_count;
    private InstagramBroadcast broadcast;
}
