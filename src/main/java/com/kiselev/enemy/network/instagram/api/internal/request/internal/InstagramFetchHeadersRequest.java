package com.kiselev.enemy.network.instagram.api.internal.request.internal;

import com.kiselev.enemy.network.instagram.api.internal.payload.StatusResult;
import com.kiselev.enemy.network.instagram.api.internal.request.InstagramGetRequest;
import com.kiselev.enemy.network.instagram.api.internal.util.InstagramGenericUtil;
import lombok.SneakyThrows;

public class InstagramFetchHeadersRequest extends InstagramGetRequest<StatusResult> {

    @Override
    public String getUrl() {
        return "si/fetch_headers/?challenge_type=signup&guid=" + InstagramGenericUtil.generateUuid(false);
    }

    @Override
    public String getPayload() {
        return null;
    }

    @Override
    public boolean requiresLogin() {
        return false;
    }

    @Override
    @SneakyThrows
    public StatusResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, StatusResult.class);
    }

}
