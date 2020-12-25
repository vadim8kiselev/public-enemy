package com.kiselev.enemy.network.instagram.api.internal.request;

import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramGetMediaInfoResult;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

@AllArgsConstructor
@Log4j
public class InstagramGetMediaInfoRequest extends InstagramGetRequest<InstagramGetMediaInfoResult> {

    private long mediaId;

    @Override
    public String getUrl() {
        return "media/" + mediaId + "/info/";
    }

    @Override
    @SneakyThrows
    public InstagramGetMediaInfoResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramGetMediaInfoResult.class);
    }
}
