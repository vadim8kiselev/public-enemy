package com.kiselev.enemy.network.instagram.api.internal.request.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiselev.enemy.network.instagram.api.internal.payload.StatusResult;
import com.kiselev.enemy.network.instagram.api.internal.request.InstagramPostRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import java.util.LinkedHashMap;
import java.util.Map;

@AllArgsConstructor
@Log4j
public class InstagramExposeRequest extends InstagramPostRequest<StatusResult> {
    @Override
    public String getUrl() {
        return "qe/expose/";
    }

    @Override
    @SneakyThrows
    public String getPayload() {

        Map<String, Object> likeMap = new LinkedHashMap<>();
        likeMap.put("_uuid", api.getUuid());
        likeMap.put("_uid", api.getUserId());
        likeMap.put("id", api.getUserId());
        likeMap.put("_csrftoken", api.getOrFetchCsrf());
        likeMap.put("experiment", "ig_android_profile_contextual_feed");

        ObjectMapper mapper = new ObjectMapper();
        String payloadJson = mapper.writeValueAsString(likeMap);

        return payloadJson;
    }

    @Override
    @SneakyThrows
    public StatusResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, StatusResult.class);
    }
}
