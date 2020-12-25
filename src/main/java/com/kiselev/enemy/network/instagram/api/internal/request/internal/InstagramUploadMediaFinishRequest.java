package com.kiselev.enemy.network.instagram.api.internal.request.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiselev.enemy.network.instagram.api.internal.constants.InstagramConstants;
import com.kiselev.enemy.network.instagram.api.internal.payload.StatusResult;
import com.kiselev.enemy.network.instagram.api.internal.request.InstagramPostRequest;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.*;
import java.util.Map.Entry;

@RequiredArgsConstructor
@AllArgsConstructor
public class InstagramUploadMediaFinishRequest extends InstagramPostRequest<StatusResult> {
    @NonNull
    private String uploadId;
    @NonNull
    private String sourceType;
    private Collection<Entry<String, Object>> extraParams;

    @Override
    public String getUrl() {
        return "media/upload_finish/?video=1";
    }

    @Override
    public boolean isSigned() {
        return true;
    }

    @Override
    @SneakyThrows
    public String getPayload() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        map.put("_csrftoken", api.getOrFetchCsrf());
        map.put("timezone_offset", String.valueOf(TimeZone.getDefault().getRawOffset()));
        map.put("_uid", api.getUserId());
        map.put("_uuid", api.getUuid());
        map.put("upload_id", uploadId);
        map.put("source_type", sourceType);
        map.put("device_id", api.getDeviceId());
        Map<String, Object> deviceMap = new LinkedHashMap<>();
        deviceMap.put("manufacturer", InstagramConstants.getDevice().getDEVICE_MANUFACTURER());
        deviceMap.put("model", InstagramConstants.getDevice().getDEVICE_MODEL());
        deviceMap.put("android_version", InstagramConstants.getDevice().getDEVICE_ANDROID_VERSION());
        deviceMap.put("android_release", InstagramConstants.getDevice().getDEVICE_ANDROID_RELEASE());
        map.put("device", deviceMap);
        extraParams.forEach(e -> map.put(e.getKey(), e.getValue()));

        return mapper.writeValueAsString(map);
    }

    @Override
    public StatusResult parseResult(int resultCode, String content) {
        return this.parseJson(resultCode, content, StatusResult.class);
    }

}
