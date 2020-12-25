package com.kiselev.enemy.network.instagram.api.internal2.requests.media;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPaginatedRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.media.MediaListReelMediaViewerResponse;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor
public class MediaListReelMediaViewerRequest extends IGGetRequest<MediaListReelMediaViewerResponse>
        implements IGPaginatedRequest<MediaListReelMediaViewerResponse> {
    @NonNull
    private String reel_id;
    @Setter
    private String max_id;

    @Override
    public String path() {
        return "media/" + reel_id + "/list_reel_media_viewer/";
    }

    @Override
    public String getQueryString(IGClient client) {
        return mapQueryString("max_id", max_id);
    }

    @Override
    public Class<MediaListReelMediaViewerResponse> getResponseType() {
        return MediaListReelMediaViewerResponse.class;
    }

}
