package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * InstagramCarouselMediaItem
 *
 * @author Justin Vo
 */

@Getter
@Setter
@ToString(callSuper = true)
public class InstagramCarouselMediaItem extends InstagramFeedItem {
    //    private String id;
//    private String media_type;
//    private int original_width;
//    private int original_height;
//    private long pk;
    private String carousel_parent_id;
}
