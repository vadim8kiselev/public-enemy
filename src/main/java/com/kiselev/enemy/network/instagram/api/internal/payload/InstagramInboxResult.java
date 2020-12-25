package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Inbox Result
 *
 * @author Krisnamourt da Silva C. Filho
 */
@Getter
@Setter
@ToString(callSuper = true)
public class InstagramInboxResult extends StatusResult {

    public String seq_id;
    public int pending_requests_total;
    public List<InstagramUser> pending_requests_users;
    public InstagramInbox inbox;
    public String status;

}
