package com.kiselev.enemy.network.instagram.api.internal2.requests.direct;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGPayload;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPostRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.direct.DirectThreadsResponse;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DirectThreadsUpdateTitleRequest extends IGPostRequest<DirectThreadsResponse> {
    @NonNull
    private String thread_id;
    @NonNull
    private String _title;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IGPayload() {
            @Getter
            private String title = _title;
        };
    }

    @Override
    public String path() {
        return "direct_v2/threads/" + thread_id + "/update_title/";
    }

    @Override
    public Class<DirectThreadsResponse> getResponseType() {
        return DirectThreadsResponse.class;
    }

}
