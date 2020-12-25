package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Get Reels Tray Result (Story)
 *
 * @author Ozan Karaali
 */

@Getter
@Setter
@ToString(callSuper = true)
public class InstagramGetReelsTrayFeedResult extends StatusResult {
    private List<InstagramStoryTray> tray;
    private List<InstagramBroadcast> broadcasts;
    private InstagramPostLive post_live;
    private int sticker_version;
    private int face_filter_nux_version;
    private boolean has_new_nux_story;
    private String story_ranking_token;

}