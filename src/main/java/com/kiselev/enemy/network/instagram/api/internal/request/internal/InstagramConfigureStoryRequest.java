package com.kiselev.enemy.network.instagram.api.internal.request.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiselev.enemy.network.instagram.api.internal.constants.InstagramConstants;
import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramConfigureStoryResult;
import com.kiselev.enemy.network.instagram.api.internal.request.InstagramPostRequest;
import com.kiselev.enemy.network.instagram.api.internal.storymetadata.StoryMetadata;
import com.kiselev.enemy.network.instagram.api.internal.util.InstagramGenericUtil;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * InstagramConfigureStoryPhotoRequest
 *
 * @author Justin Vo
 */
@RequiredArgsConstructor
@AllArgsConstructor
public class InstagramConfigureStoryRequest extends InstagramPostRequest<InstagramConfigureStoryResult> {
    @NonNull
    private File mediaFile;
    @NonNull
    private String uploadId;
    private String threadId;

    private Collection<StoryMetadata> metadata;

    @Override
    public String getUrl() {
        return "media/configure_to_story/";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    @SneakyThrows
    public String getPayload() {
        boolean direct = threadId != null;

        Map<String, Object> likeMap = new LinkedHashMap<>();
        likeMap.put("_csrftoken", api.getOrFetchCsrf());
        likeMap.put("_uid", api.getUserId());
        likeMap.put("_uuid", api.getUuid());
        likeMap.put("upload_id", uploadId);

        Map<String, Object> deviceMap = new LinkedHashMap<>();
        deviceMap.put("manufacturer", InstagramConstants.getDevice().getDEVICE_MANUFACTURER());
        deviceMap.put("model", InstagramConstants.getDevice().getDEVICE_MODEL());
        deviceMap.put("android_version", InstagramConstants.getDevice().getDEVICE_ANDROID_VERSION());
        deviceMap.put("android_release", InstagramConstants.getDevice().getDEVICE_ANDROID_RELEASE());
        likeMap.put("device", deviceMap);

        Map<String, Object> editsMap = new LinkedHashMap<>();
        Dimension image = InstagramGenericUtil.getImageDimension(mediaFile);
        editsMap.put("crop_original_size", Arrays.asList((double) image.getWidth(), (double) image.getHeight()));
        editsMap.put("crop_center", Arrays.asList((double) 0, (double) -0));
        editsMap.put("crop_zoom", 1.0);
        likeMap.put("edits", editsMap);

        Map<String, Object> extraMap = new LinkedHashMap<>();

        extraMap.put("source_width", image.getWidth());
        extraMap.put("source_height", image.getHeight());
        likeMap.put("extra", extraMap);

        likeMap.put("source_type", "3");
        likeMap.put("configure_mode", direct ? "2" : "1");

        if (metadata != null) {
            applyMetadata(likeMap, metadata);
        }

        if (direct) {
            likeMap.put("thread_ids", new ObjectMapper().writeValueAsString(Arrays.asList(threadId)));
        }

        ObjectMapper mapper = new ObjectMapper();
        String payloadJson = mapper.writeValueAsString(likeMap);

        return payloadJson;
    }

    @Override
    @SneakyThrows
    public InstagramConfigureStoryResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramConfigureStoryResult.class);
    }

    private void applyMetadata(Map<String, Object> toApplyTo, Collection<StoryMetadata> metadatas) {

        for (StoryMetadata metadata : metadatas) {
            if (metadata.check())
                toApplyTo.put(metadata.key(), metadata.metadata());
        }

    }
}
