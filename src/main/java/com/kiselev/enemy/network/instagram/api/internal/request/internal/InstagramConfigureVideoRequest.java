package com.kiselev.enemy.network.instagram.api.internal.request.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiselev.enemy.network.instagram.api.internal.constants.InstagramConstants;
import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramConfigureMediaResult;
import com.kiselev.enemy.network.instagram.api.internal.request.InstagramPostRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.*;

@AllArgsConstructor
@Builder
public class InstagramConfigureVideoRequest extends InstagramPostRequest<InstagramConfigureMediaResult> {

    private String uploadId;
    private String caption;
    private long duration;

    @Override
    public String getUrl() {
        return "media/configure/?video=1";
    }

    @Override
    public boolean isSigned() {
        return true;
    }

    @Override
    @SneakyThrows
    public String getPayload() {

        Map<String, Object> likeMap = new LinkedHashMap<>();
        likeMap.put("timezone_offset", TimeZone.getDefault().getRawOffset());
        likeMap.put("_csrftoken", api.getOrFetchCsrf());
        likeMap.put("source_type", "4");
        likeMap.put("_uid", api.getUserId());
        likeMap.put("device_id", api.getDeviceId());
        likeMap.put("_uuid", api.getUuid());
        likeMap.put("upload_id", uploadId);
        Map<String, Object> deviceMap = new LinkedHashMap<>();
        deviceMap.put("manufacturer", InstagramConstants.getDevice().getDEVICE_MANUFACTURER());
        deviceMap.put("model", InstagramConstants.getDevice().getDEVICE_MODEL());
        deviceMap.put("android_version", InstagramConstants.getDevice().getDEVICE_ANDROID_VERSION());
        deviceMap.put("android_release", InstagramConstants.getDevice().getDEVICE_ANDROID_RELEASE());
        likeMap.put("device", deviceMap);
        likeMap.put("length", duration);
        List<Object> clips = Arrays.asList(new Object() {
            @Getter
            private String length = String.valueOf(duration);
            @Getter
            private String source_type = "4";
        });
        likeMap.put("clips", clips);
        likeMap.put("poster_frame_index", 0);
        likeMap.put("audio_muted", false);
        if (caption != null && !caption.isEmpty()) {
            likeMap.put("caption", caption);
        }

        ObjectMapper mapper = new ObjectMapper();
        String payloadJson = mapper.writeValueAsString(likeMap);

        return payloadJson;
    }

    @Override
    @SneakyThrows
    public InstagramConfigureMediaResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramConfigureMediaResult.class);
    }

}
