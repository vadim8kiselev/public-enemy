package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * InstagramSticker
 *
 * @author Justin
 */
@Getter
@Setter
public class InstagramSticker {
    private String id;
    private List<InstagramStickerMetadata> stickers;
    private List<String> keywords;
}
