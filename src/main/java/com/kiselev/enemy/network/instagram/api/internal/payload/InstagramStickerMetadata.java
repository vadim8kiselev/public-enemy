package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;

/**
 * InstagramStickerMetadata
 *
 * @author Justin
 */
@Getter
@Setter
public class InstagramStickerMetadata {
    private String id;
    private String type;
    private String name;
    private String image_url;
    private double image_width_ratio;
    private double tray_image_width_ratio;
    private double image_width;
    private double image_height;
}
