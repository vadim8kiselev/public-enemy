package com.kiselev.enemy.network.instagram.api.internal2.responses.feed;

import java.util.List;

import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGPaginatedResponse;

import lombok.Data;

@Data
public class FeedUsersResponse extends IGPaginatedResponse {
    private List<Profile> users;
    private String next_max_id;

    public boolean isMore_available() {
        return next_max_id != null;
    }
}
