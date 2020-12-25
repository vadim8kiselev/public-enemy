package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Inbox Thread
 *
 * @author Krisnamourt da Silva C. Filho
 */
@Getter
@Setter
@ToString(callSuper = true)
public class InstagramInboxThread {

    public String thread_id;
    public String thread_v2_id;
    public InstagramUser inviter;
    public List<InstagramUser> users;
    public List<InstagramUser> left_users;
    public List<InstagramInboxThreadItem> items;
    public long last_activity_at;
    public boolean muted;
    public boolean is_pin;
    public boolean named;
    public boolean canonical;
    public boolean pending;
    public boolean valued_request;
    public String thread_type;
    public long viewer_id;
    public String thread_title;
    public long pending_score;
    public int reshare_send_count;
    public int reshare_receive_count;
    public int expiring_media_send_count;
    public int expiring_media_receive_count;
    public boolean has_older;
    public boolean has_newer;
    public String newest_cursor;
    public String oldest_cursor;
    public boolean is_spam;
}
