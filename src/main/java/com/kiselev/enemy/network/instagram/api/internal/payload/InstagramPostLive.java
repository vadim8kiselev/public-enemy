package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * PostLive
 *
 * @author Ozan Karaali
 */

@Getter
@Setter
@ToString(callSuper = true)
public class InstagramPostLive extends StatusResult {
    List<InstagramPostLiveItem> post_live_items;
}

