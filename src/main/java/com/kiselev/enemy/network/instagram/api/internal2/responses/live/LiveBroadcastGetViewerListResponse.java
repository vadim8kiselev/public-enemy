package com.kiselev.enemy.network.instagram.api.internal2.responses.live;

import java.util.List;

import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;

import lombok.Data;

@Data
public class LiveBroadcastGetViewerListResponse extends IGResponse {
    private List<Profile> users;
}
