package com.kiselev.enemy.network.instagram.api.internal2.requests.igtv;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGPayload;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPostRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.igtv.IgtvSearchResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class IgtvSearchRequest extends IGPostRequest<IgtvSearchResponse> {
    private String _query;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IgtvSearchPayload();
    }

    @Override
    public String path() {
        return String.format("igtv/%s/", _query != null ? "search" : "suggested_searches");
    }

    @Override
    public Class<IgtvSearchResponse> getResponseType() {
        return IgtvSearchResponse.class;
    }

    @Data
    @JsonInclude(Include.NON_NULL)
    public class IgtvSearchPayload extends IGPayload {
        private String query = _query;
    }

}
