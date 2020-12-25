package com.kiselev.enemy.network.instagram.api.internal.request;

import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramGetChallengeResult;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;

@AllArgsConstructor
public class InstagramGetChallengeRequest extends InstagramGetRequest<InstagramGetChallengeResult> {

    @NonNull
    private final String challengeUrl;

    @Override
    public String getUrl() {
        return challengeUrl;
    }

    @Override
    @SneakyThrows
    public InstagramGetChallengeResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramGetChallengeResult.class);
    }
}