package com.kiselev.enemy.network.instagram.api.internal2.requests.direct;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.direct.DirectInboxResponse;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor
public class DirectPendingInboxRequest extends IGGetRequest<DirectInboxResponse> {
    @NonNull
    private String cursor;

    @Override
    public String path() {
        return "direct_v2/pending_inbox/";
    }

    @Override
    public String getQueryString(IGClient client) {
        return mapQueryString("cursor", cursor);
    }

    @Override
    public Class<DirectInboxResponse> getResponseType() {
        return DirectInboxResponse.class;
    }

}
