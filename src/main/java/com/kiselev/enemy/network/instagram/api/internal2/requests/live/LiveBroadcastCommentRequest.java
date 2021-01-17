package com.kiselev.enemy.network.instagram.api.internal2.requests.live;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGPayload;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPostRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.live.LiveBroadcastCommentResponse;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LiveBroadcastCommentRequest extends IGPostRequest<LiveBroadcastCommentResponse> {
    @NonNull
    private String broadcast_id, _message;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new LiveCommentPayload();
    }

    @Override
    public String path() {
        return "live/" + broadcast_id + "/comment/";
    }

    @Override
    public Class<LiveBroadcastCommentResponse> getResponseType() {
        return LiveBroadcastCommentResponse.class;
    }

    @Data
    public class LiveCommentPayload extends IGPayload {
        private String comment_text = _message;
    }

}
