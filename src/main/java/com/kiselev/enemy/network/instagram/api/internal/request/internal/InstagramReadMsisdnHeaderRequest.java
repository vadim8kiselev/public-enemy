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
public class InstagramReadMsisdnHeaderRequest extends InstagramPostRequest<StatusResult> {

    @Override
    public String getUrl() {
        return "accounts/read_msisdn_header/";
    }

    @Override
    @SneakyThrows
    public String getPayload() {

        Map<String, Object> likeMap = new LinkedHashMap<>();
        likeMap.put("device_id", api.getUuid());
        likeMap.put("_csrftoken", api.getOrFetchCsrf());

        ObjectMapper mapper = new ObjectMapper();
        String payloadJson = mapper.writeValueAsString(likeMap);

        return payloadJson;
    }

    @Override
    @SneakyThrows
    public StatusResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, StatusResult.class);
    }

    /**
     * @return if request must be logged in
     */
    @Override
    public boolean requiresLogin() {
        return false;
    }
}