package com.kiselev.enemy.network.instagram.api.internal.request.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiselev.enemy.network.instagram.api.internal.constants.InstagramConstants;
import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramSyncFeaturesResult;
import com.kiselev.enemy.network.instagram.api.internal.request.InstagramPostRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import java.util.LinkedHashMap;
import java.util.Map;

@AllArgsConstructor
@Log4j
public class InstagramSyncFeaturesRequest extends InstagramPostRequest<InstagramSyncFeaturesResult> {

    private boolean preLogin = false;

    @Override
    public String getUrl() {
        return "qe/sync/";
    }

    @Override
    @SneakyThrows
    public String getPayload() {

        Map<String, Object> likeMap = new LinkedHashMap<>();
        likeMap.put("id", api.getUuid());
        likeMap.put("experiments", InstagramConstants.DEVICE_EXPERIMENTS);

        if (!preLogin) {
            likeMap.put("_uuid", api.getUuid());
            likeMap.put("_uid", api.getUserId());
            likeMap.put("_csrftoken", api.getOrFetchCsrf());

        }

        ObjectMapper mapper = new ObjectMapper();
        String payloadJson = mapper.writeValueAsString(likeMap);

        return payloadJson;
    }

    @Override
    @SneakyThrows
    public InstagramSyncFeaturesResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramSyncFeaturesResult.class);
    }

    /**
     * @return if request must be logged in
     */
    @Override
    public boolean requiresLogin() {
        return false;
    }

}
