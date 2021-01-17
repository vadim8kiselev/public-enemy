package com.kiselev.enemy.network.instagram.api.internal2.requests.live;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGPayload;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPostRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.live.LiveBroadcastHeartbeatResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LiveBroadcastHeartbeatRequest extends IGPostRequest<LiveBroadcastHeartbeatResponse> {
    @NonNull
    private String broadcast_id;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IGPayload();
    }

    @Override
    public String path() {
        return "live/" + broadcast_id + "/heartbeat_and_get_viewer_count/";
    }

    @Override
    public Class<LiveBroadcastHeartbeatResponse> getResponseType() {
        return LiveBroadcastHeartbeatResponse.class;
    }

}
