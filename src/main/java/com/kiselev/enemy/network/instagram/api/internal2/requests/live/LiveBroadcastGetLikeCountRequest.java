package com.kiselev.enemy.network.instagram.api.internal2.requests.live;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.live.LiveBroadcastLikeResponse.LiveBroadcastGetLikeCountResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class LiveBroadcastGetLikeCountRequest
        extends IGGetRequest<LiveBroadcastGetLikeCountResponse> {
    @NonNull
    private String broadcast_id;
    private long like_ts;

    @Override
    public String path() {
        return "live/" + broadcast_id + "/get_like_count/";
    }

    @Override
    public String getQueryString(IGClient client) {
        return mapQueryString("like_ts", String.valueOf(like_ts));
    }

    @Override
    public Class<LiveBroadcastGetLikeCountResponse> getResponseType() {
        return LiveBroadcastGetLikeCountResponse.class;
    }

}
