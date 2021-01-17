package com.kiselev.enemy.network.instagram.api.internal.request;

import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramFeedResult;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
@RequiredArgsConstructor
public class InstagramUserFeedRequest extends InstagramGetRequest<InstagramFeedResult> {

    private long userId;
    private String maxId;
    private long minTimestamp;
    private long maxTimestamp;


    @Override
    public String getUrl() {
        return "feed/user/" + userId + "/?max_id=" + maxId + "&min_timestamp=" + minTimestamp + "&max_timestamp=" + maxTimestamp + "&rank_token=" + api.getRankToken() + "&ranked_content=true&";
    }

    @Override
    @SneakyThrows
    public InstagramFeedResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramFeedResult.class);
    }

}
