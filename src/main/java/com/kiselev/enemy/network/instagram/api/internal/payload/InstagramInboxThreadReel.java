package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Inbox Thread Reel
 *
 * @author Krisnamourt da Silva C. Filho
 */
@Getter
@Setter
@ToString(callSuper = true)
public class InstagramInboxThreadReel {
    public String text;
    public String type;
    public String mentioned_user_id;
    public Boolean can_repost;
    public InstagramFeedItem media;
    public InstagramFeedItem media_share;


}
