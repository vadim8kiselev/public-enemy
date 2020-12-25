package com.kiselev.enemy.network.instagram.api.internal2.responses.direct;

import com.kiselev.enemy.network.instagram.api.internal2.models.direct.Inbox;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.User;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;

import lombok.Data;

@Data
public class DirectInboxResponse extends IGResponse {
    private User viewer;
    private Inbox inbox;
    private int seq_id;
    private int pending_requests_total;
    private User most_recent_inviter;
}
