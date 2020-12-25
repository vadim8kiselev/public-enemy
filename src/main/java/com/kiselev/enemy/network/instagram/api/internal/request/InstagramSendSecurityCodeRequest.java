package com.kiselev.enemy.network.instagram.api.internal.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramLoginResult;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * InstagramSendSecurityCodeRequest.
 *
 * @author evosystem
 */
@AllArgsConstructor
public class InstagramSendSecurityCodeRequest extends InstagramPostRequest<InstagramLoginResult> {

    @NonNull
    private final String challengeUrl;

    @NonNull
    private final String securityCode;

    @Override
    public String getUrl() {
        return challengeUrl;
    }

    @Override
    @SneakyThrows
    public String getPayload() {
        Map<String, Object> likeMap = new LinkedHashMap<>();
        likeMap.put("security_code", securityCode);
        likeMap.put("_csrftoken", api.getOrFetchCsrf());
        likeMap.put("guid", api.getUuid());
        likeMap.put("device_id", api.getDeviceId());

        ObjectMapper mapper = new ObjectMapper();
        String payloadJson = mapper.writeValueAsString(likeMap);

        return payloadJson;
    }

    @Override
    @SneakyThrows
    public InstagramLoginResult parseResult(int statusCode, String content) {
        return this.parseJson(statusCode, content, InstagramLoginResult.class);
    }
}