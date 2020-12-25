package com.kiselev.enemy.network.instagram.api.internal.request.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiselev.enemy.network.instagram.api.internal.constants.InstagramConstants;
import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramConfigureMediaResult;
import com.kiselev.enemy.network.instagram.api.internal.request.InstagramPostRequest;
import com.kiselev.enemy.network.instagram.api.internal.util.InstagramGenericUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@AllArgsConstructor
public class InstagramConfigurePhotoRequest extends InstagramPostRequest<InstagramConfigureMediaResult> {
    private File file;
    private String uploadId;
    private String caption;

    @Override
    public String getUrl() {
        return "media/configure/?";
    }

    @Override
    @SneakyThrows
    public String getPayload() {

        Map<String, Object> likeMap = new LinkedHashMap<>();
        likeMap.put("_csrftoken", api.getOrFetchCsrf());
        likeMap.put("media_folder", "Instagram");
        likeMap.put("source_type", 4);
        likeMap.put("_uid", api.getUserId());
        likeMap.put("_uuid", api.getUuid());
        likeMap.put("caption", caption);
        likeMap.put("upload_id", uploadId);

        Map<String, Object> deviceMap = new LinkedHashMap<>();
        deviceMap.put("manufacturer", InstagramConstants.getDevice().getDEVICE_MANUFACTURER());
        deviceMap.put("model", InstagramConstants.getDevice().getDEVICE_MODEL());
        deviceMap.put("android_version", InstagramConstants.getDevice().getDEVICE_ANDROID_VERSION());
        deviceMap.put("android_release", InstagramConstants.getDevice().getDEVICE_ANDROID_RELEASE());
        likeMap.put("device", deviceMap);

        Map<String, Object> editsMap = new LinkedHashMap<>();
        Dimension image = InstagramGenericUtil.getImageDimension(file);
        editsMap.put("crop_original_size", Arrays.asList((double) image.getWidth(), (double) image.getHeight()));
        editsMap.put("crop_center", Arrays.asList((double) 0, (double) 0));
        editsMap.put("crop_zoom", 1.0);
        likeMap.put("edits", editsMap);

        Map<String, Object> extraMap = new LinkedHashMap<>();
        extraMap.put("source_width", image.getWidth());
        extraMap.put("source_height", image.getHeight());
        likeMap.put("extra", extraMap);

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
