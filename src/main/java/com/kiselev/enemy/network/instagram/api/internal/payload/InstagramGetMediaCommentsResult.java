package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Response from media comments request
 * <p>
 * Evgeny Bondarenko (evgbondarenko@gmail.com)
 */
@Getter
@Setter
@ToString(callSuper = true)
public class InstagramGetMediaCommentsResult extends StatusResult {
    private boolean comment_likes_enabled;
    private int comment_count;
    private boolean caption_is_edited;
    private boolean has_more_comments;
    private boolean has_more_headload_comments;
    private String next_max_id;
    private List<InstagramComment> comments;
}
