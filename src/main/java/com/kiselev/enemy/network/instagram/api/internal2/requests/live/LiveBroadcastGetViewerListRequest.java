package com.kiselev.enemy.network.instagram.api.internal2.requests.live;

import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.live.LiveBroadcastGetViewerListResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LiveBroadcastGetViewerListRequest
        extends IGGetRequest<LiveBroadcastGetViewerListResponse> {
    @NonNull
    private String broadcast_id;

    @Override
    public String path() {
        return "live/" + broadcast_id + "/get_viewer_list/";
    }

    @Override
    public Class<LiveBroadcastGetViewerListResponse> getResponseType() {
        return LiveBroadcastGetViewerListResponse.class;
    }

}
