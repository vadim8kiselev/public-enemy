package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;

/**
 * InstagramConfigureStoryResult
 *
 * @author Justin Vo
 */
@Getter
@Setter
public class InstagramConfigureStoryResult extends StatusResult {
    private InstagramStoryItem media;
    private String upload_id;
}
