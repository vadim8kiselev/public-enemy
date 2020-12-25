package com.kiselev.enemy.network.instagram.api.internal.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramGetChallengeResult;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class InstagramResetChallengeRequest extends InstagramPostRequest<InstagramGetChallengeResult> {

    @NonNull
    private final String challengeUrl;

    @Override
    public String getUrl() {
        return challengeUrl;
    }

    @SneakyThrows
    @Override
    public String getPayload() {
        Map<String, Object> map = new HashMap<>();
        map.put("_csrftoken", api.getOrFetchCsrf());
        map.put("guid", api.getUuid());
        map.put("device_id", api.getDeviceId());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(map);
    }

    @Override
    public InstagramGetChallengeResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramGetChallengeResult.class);
    }
}