package com.kiselev.enemy.network.instagram.api.internal2.requests.live;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.live.LiveBroadcastGetCommentResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class LiveBroadcastGetCommentRequest extends IGGetRequest<LiveBroadcastGetCommentResponse> {
    @NonNull
    private String broadcast_id;
    private long last_ts;

    @Override
    public String path() {
        return "live/" + broadcast_id + "/get_comment/";
    }

    @Override
    public String getQueryString(IGClient client) {
        return mapQueryString("last_comment_ts", String.valueOf(last_ts));
    }

    @Override
    public Class<LiveBroadcastGetCommentResponse> getResponseType() {
        return LiveBroadcastGetCommentResponse.class;
    }

}
