package com.kiselev.enemy.network.instagram.api.internal2.requests.music;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGPayload;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPostRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.music.MusicBrowseResponse;

import lombok.Getter;

public class MusicBrowseRequest extends IGPostRequest<MusicBrowseResponse> {

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IGPayload() {
            @Getter
            private String session_id = client.getSessionId();
        };
    }

    @Override
    public String path() {
        return "music/browse/";
    }

    @Override
    public Class<MusicBrowseResponse> getResponseType() {
        return MusicBrowseResponse.class;
    }

}
