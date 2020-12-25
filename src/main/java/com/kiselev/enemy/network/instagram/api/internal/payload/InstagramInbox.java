package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Inbox
 *
 * @author Krisnamourt da Silva C. Filho
 */
@Getter
@Setter
@ToString
public class InstagramInbox {

    public boolean has_older;
    public int unseen_count;
    public String unseen_count_ts;
    public String oldest_cursor;
    public boolean blended_inbox_enabled;
    public List<InstagramInboxThread> threads;


}