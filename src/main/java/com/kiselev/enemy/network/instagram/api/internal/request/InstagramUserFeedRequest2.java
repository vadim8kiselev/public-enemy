package com.kiselev.enemy.network.instagram.api.internal.request;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramFeedResult;
import com.kiselev.enemy.network.instagram.api.internal.request.InstagramGetRequest;

@AllArgsConstructor
@RequiredArgsConstructor
public class InstagramUserFeedRequest2 extends InstagramGetRequest<InstagramFeedResult> {

    private final Long userId;

    private final String maxId;

    private Long minTimestamp;

    private Long maxTimestamp;

    @Override
    public String getUrl() {
        return "feed/user/" + userId +
                "/?max_id=" + maxId +
                "&min_timestamp=" + minTimestamp +
                "&max_timestamp=" + maxTimestamp +
                "&rank_token=" + api.getRankToken() +
                "&ranked_content=true&";
    }

    @Override
    public InstagramFeedResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramFeedResult.class);
    }
}