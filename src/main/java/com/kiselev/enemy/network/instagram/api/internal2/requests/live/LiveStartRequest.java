package com.kiselev.enemy.network.instagram.api.internal2.requests.live;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGPayload;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPostRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.live.LiveStartResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class LiveStartRequest extends IGPostRequest<LiveStartResponse> {
    private String broadcastId;
    private boolean sendNotification;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IGPayload() {
            @Getter
            private boolean should_send_notifications = sendNotification;
        };
    }

    @Override
    public String path() {
        return "live/" + broadcastId + "/start/";
    }

    @Override
    public Class<LiveStartResponse> getResponseType() {
        return LiveStartResponse.class;
    }

}
