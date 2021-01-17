package com.kiselev.enemy.network.instagram.api.internal2.requests.direct;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.direct.DirectThreadsResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class DirectThreadsRequest extends IGGetRequest<DirectThreadsResponse> {
    @NonNull
    private String thread_id;
    private String cursor;

    @Override
    public String path() {
        return "direct_v2/threads/" + thread_id + "/";
    }

    @Override
    public String getQueryString(IGClient client) {
        return mapQueryString("cursor", cursor);
    }

    @Override
    public Class<DirectThreadsResponse> getResponseType() {
        return DirectThreadsResponse.class;
    }

}
