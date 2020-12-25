package com.kiselev.enemy.network.instagram.api.internal.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramSelectVerifyMethodResult;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * InstagramSelectVerifyMethodRequest.
 *
 * @author evosystem
 */
@AllArgsConstructor
public class InstagramSelectVerifyMethodRequest extends InstagramPostRequest<InstagramSelectVerifyMethodResult> {

    @NonNull
    private final String challengeUrl;

    private final int choice;

    @Override
    public String getUrl() {
        return challengeUrl;
    }

    @Override
    @SneakyThrows
    public String getPayload() {
        Map<String, Object> likeMap = new LinkedHashMap<>();
        likeMap.put("choice", choice);
        likeMap.put("_csrftoken", api.getOrFetchCsrf());
        likeMap.put("guid", api.getUuid());
        likeMap.put("device_id", api.getDeviceId());

        ObjectMapper mapper = new ObjectMapper();
        String payloadJson = mapper.writeValueAsString(likeMap);

        return payloadJson;
    }

    @Override
    @SneakyThrows
    public InstagramSelectVerifyMethodResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramSelectVerifyMethodResult.class);
    }
}