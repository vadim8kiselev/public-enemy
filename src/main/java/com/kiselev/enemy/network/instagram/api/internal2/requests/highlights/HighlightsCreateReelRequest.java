package com.kiselev.enemy.network.instagram.api.internal2.requests.highlights;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGPayload;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPostRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.highlights.HighlightsCreateReelResponse;
import com.kiselev.enemy.network.instagram.api.internal2.utils.IGUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
public class HighlightsCreateReelRequest extends IGPostRequest<HighlightsCreateReelResponse> {
    @NonNull
    private String _title;
    @NonNull
    private String[] _media_ids;
    private String _cover_media_id;

    public HighlightsCreateReelRequest(String title, String... media_ids) {
        this._title = title;
        this._media_ids = media_ids;
        this._cover_media_id = this._media_ids[0];
    }

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new HighlightsCreateReelPayload();
    }

    @Override
    public String path() {
        return "highlights/create_reel/";
    }

    @Override
    public Class<HighlightsCreateReelResponse> getResponseType() {
        return HighlightsCreateReelResponse.class;
    }

    @Data
    public class HighlightsCreateReelPayload extends IGPayload {
        private String creation_id = String.valueOf(System.currentTimeMillis());
        private String title = _title;
        private String cover = IGUtils.objectToJson(new Object() {
            @Getter
            private String media_id = _cover_media_id;
        });
        private String media_ids = IGUtils.objectToJson(_media_ids);
    }

}
