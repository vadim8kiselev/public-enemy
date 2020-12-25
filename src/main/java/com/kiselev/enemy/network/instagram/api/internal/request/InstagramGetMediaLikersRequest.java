package com.kiselev.enemy.network.instagram.api.internal.request;

import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramGetMediaLikersResult;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

@AllArgsConstructor
@Log4j
public class InstagramGetMediaLikersRequest extends InstagramGetRequest<InstagramGetMediaLikersResult> {

    private String mediaId;

    @Override
    public String getUrl() {
        return "media/" + mediaId + "/likers/";
    }

    @Override
    @SneakyThrows
    public InstagramGetMediaLikersResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramGetMediaLikersResult.class);
    }
}
