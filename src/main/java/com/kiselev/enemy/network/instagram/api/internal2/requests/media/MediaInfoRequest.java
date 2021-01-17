package com.kiselev.enemy.network.instagram.api.internal2.requests.media;

import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.media.MediaInfoResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MediaInfoRequest extends IGGetRequest<MediaInfoResponse> {
    @NonNull
    private String id;

    @Override
    public String path() {
        return "media/" + id + "/info/";
    }

    @Override
    public Class<MediaInfoResponse> getResponseType() {
        return MediaInfoResponse.class;
    }

}
