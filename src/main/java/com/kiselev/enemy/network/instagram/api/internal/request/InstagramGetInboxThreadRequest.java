package com.kiselev.enemy.network.instagram.api.internal.request;

import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramInboxThreadResult;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;

/**
 * Inbox Thread Request
 *
 * @author Krisnamourt da Silva C. Filho
 */
@AllArgsConstructor
public class InstagramGetInboxThreadRequest extends InstagramGetRequest<InstagramInboxThreadResult> {

    @NonNull
    private String threadId;
    private String cursor;

    @Override
    public String getUrl() {
        String baseUrl = "direct_v2/threads/" + threadId + "/?";
        if (cursor != null && !cursor.isEmpty()) {
            baseUrl += "&cursor=" + cursor;
        }
        return baseUrl;
    }

    @Override
    public String getPayload() {
        return null;
    }

    @Override
    @SneakyThrows
    public InstagramInboxThreadResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramInboxThreadResult.class);
    }

}
