package com.kiselev.enemy.network.instagram.api.internal.request;

import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramInboxResult;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
@AllArgsConstructor
public class InstagramGetInboxRequest extends InstagramGetRequest<InstagramInboxResult> {

    private String cursor;

    @Override
    public String getUrl() {

        String baseUrl = "direct_v2/inbox/?";
        if (cursor != null && !cursor.isEmpty()) {
            baseUrl += "&cursor=" + cursor;
        }
        return baseUrl;
    }

    @Override
    @SneakyThrows
    public String getPayload() {
        return null;
    }

    @Override
    @SneakyThrows
    public InstagramInboxResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramInboxResult.class);
    }

}
