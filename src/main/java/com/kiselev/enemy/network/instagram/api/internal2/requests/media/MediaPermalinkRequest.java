package com.kiselev.enemy.network.instagram.api.internal2.requests.media;

import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.media.MediaPermalinkResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MediaPermalinkRequest extends IGGetRequest<MediaPermalinkResponse> {
    @NonNull
    private String media_id;
    
    @Override
    public String path() {
        return "media/" + media_id + "/permalink/";
    }

    @Override
    public Class<MediaPermalinkResponse> getResponseType() {
        return MediaPermalinkResponse.class;
    }

}
