package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Inbox Thread Item
 *
 * @author Krisnamourt da Silva C. Filho
 */
@Getter
@Setter
@ToString(callSuper = true)
public class InstagramInboxThreadItem {

    public String item_id;
    public String user_id;
    public long timestamp;
    public String item_type;
    public String like;
    public String text;
    public InstagramInboxThreadReel reel_share;
}
