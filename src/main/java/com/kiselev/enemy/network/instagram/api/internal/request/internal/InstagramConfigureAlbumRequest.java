package com.kiselev.enemy.network.instagram.api.internal.request.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramConfigureAlbumResult;
import com.kiselev.enemy.network.instagram.api.internal.request.InstagramPostRequest;
import lombok.*;

import java.util.*;

/**
 * InstagramConfigureAlbumRequest
 *
 * @author Justin Vo
 */

@RequiredArgsConstructor
public class InstagramConfigureAlbumRequest extends InstagramPostRequest<InstagramConfigureAlbumResult> {
    @NonNull
    private List<AlbumChildrenMetadata> children_meta;
    @NonNull
    private String caption;

    @Override
    public String getUrl() {
        return "media/configure_sidecar/";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    @SneakyThrows
    public String getPayload() {
        Map<String, Object> pMap = new LinkedHashMap<>();
        pMap.put("_csrftoken", api.getOrFetchCsrf());
        pMap.put("_uid", api.getUserId());
        pMap.put("_uuid", api.getUuid());
        pMap.put("client_sidecar_id", System.currentTimeMillis());
        pMap.put("caption", caption);
        List<Object> children = new ArrayList<>();
        for (AlbumChildrenMetadata id : children_meta) {
            Map<String, Object> photoConfig = new HashMap<>();
            photoConfig.put("upload_id", id.getUploadId());
            photoConfig.put("height", id.getHeight());
            photoConfig.put("width", id.getWidth());
            if (id.isVideo()) {
                photoConfig.put("length", id.getDuration());
            }
            children.add(photoConfig);
        }
        pMap.put("children_metadata", children);

        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(pMap);
    }

    @Override
    public InstagramConfigureAlbumResult parseResult(int resultCode, String content) {
        return parseJson(resultCode, content, InstagramConfigureAlbumResult.class);
    }

    @Getter
    @Setter
    @Builder
    public static class AlbumChildrenMetadata {
        private String uploadId;
        @Builder.Default
        private boolean isVideo = false;
        private double height;
        private double width;
        private long duration;
    }

}
