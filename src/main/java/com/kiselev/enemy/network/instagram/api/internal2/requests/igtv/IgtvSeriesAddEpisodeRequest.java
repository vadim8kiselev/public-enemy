package com.kiselev.enemy.network.instagram.api.internal2.requests.igtv;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGPayload;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPostRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.igtv.IgtvSeriesResponse;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IgtvSeriesAddEpisodeRequest extends IGPostRequest<IgtvSeriesResponse> {
    @NonNull
    private String _series;
    @NonNull
    private Long pk;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IgtvSeriesAddEpisodePayload();
    }

    @Override
    public String path() {
        return "igtv/series/" + _series + "/add_episode/";
    }

    @Override
    public Class<IgtvSeriesResponse> getResponseType() {
        return IgtvSeriesResponse.class;
    }

    @Data
    public class IgtvSeriesAddEpisodePayload extends IGPayload {
        private String media_id = pk.toString();
    }

}
