package com.kiselev.enemy.network.instagram.api.internal2.requests.upload;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGPayload;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPostRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MediaUploadFinishRequest extends IGPostRequest<IGResponse> {
    @NonNull
    private String uploadId;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new MediaUploadFinishPayload();
    }

    @Override
    public String path() {
        return "media/upload_finish/";
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

    @Data
    public class MediaUploadFinishPayload extends IGPayload {
        private String upload_id = uploadId;
    }
}
