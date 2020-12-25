package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * InstagramStickerRequestResult
 *
 * @author Justin
 */
@Getter
@Setter
public class InstagramStickerRequestResult extends StatusResult {
    List<InstagramSticker> static_stickers;
}
