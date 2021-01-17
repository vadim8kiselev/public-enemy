package com.kiselev.enemy.network.instagram.api.internal2.requests.igtv;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGPayload;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPostRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.igtv.IgtvSeriesResponse;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IgtvSeriesCreateRequest extends IGPostRequest<IgtvSeriesResponse> {
    @NonNull
    private String _title, _description;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IgtvSeriesCreatePayload();
    }

    @Override
    public String path() {
        return "igtv/series/create/";
    }

    @Override
    public Class<IgtvSeriesResponse> getResponseType() {
        return IgtvSeriesResponse.class;
    }

    @Data
    public class IgtvSeriesCreatePayload extends IGPayload {
        private String title = _title;
        private String description = _description;
    }

}
