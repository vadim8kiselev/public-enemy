package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * InstagramConfigureAlbumResult
 *
 * @author Justin Vo
 */

@Getter
@Setter
@ToString(callSuper = true)
public class InstagramConfigureAlbumResult extends InstagramConfigureMediaResult {
    private String client_sidecar_id;
}
