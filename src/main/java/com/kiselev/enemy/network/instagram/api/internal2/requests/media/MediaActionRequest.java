package com.kiselev.enemy.network.instagram.api.internal2.requests.media;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGPayload;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPostRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MediaActionRequest extends IGPostRequest<IGResponse> {
    @NonNull
    private String _media_id;
    @NonNull
    private MediaAction action;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IGPayload() {
            @Getter
            private String media_id = _media_id;
        };
    }

    @Override
    public String path() {
        return String.format("media/%s/%s/", _media_id, action.name().toLowerCase());
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

    public static enum MediaAction {
        SAVE, UNSAVE, ONLY_ME, UNDO_ONLY_ME, DELETE, LIKE, UNLIKE, ENABLE_COMMENTS, DISABLE_COMMENTS;
    }

}
