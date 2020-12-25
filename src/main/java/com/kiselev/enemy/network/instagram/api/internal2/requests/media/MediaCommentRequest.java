package com.kiselev.enemy.network.instagram.api.internal2.requests.media;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGPayload;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPostRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class MediaCommentRequest extends IGPostRequest<IGResponse> {
    @NonNull
    private String id, _comment_text;
    private String _replied_to_comment_id;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new MediaCommentPayload();
    }

    @Override
    public String path() {
        return "media/" + id + "/comment/";
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

    @Data
    @JsonInclude(Include.NON_NULL)
    private class MediaCommentPayload extends IGPayload {
        private String comment_text = _comment_text;
        private String replied_to_comment_id = _replied_to_comment_id;
    }

}