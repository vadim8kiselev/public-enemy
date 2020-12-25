package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class InstagramConfigureMediaResult extends StatusResult {
    private InstagramFeedItem media;
    private String upload_id;
}
