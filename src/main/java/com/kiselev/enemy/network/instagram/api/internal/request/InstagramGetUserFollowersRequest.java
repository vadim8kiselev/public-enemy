package com.kiselev.enemy.network.instagram.api.internal.request;

import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramGetUserFollowersResult;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
@AllArgsConstructor
public class InstagramGetUserFollowersRequest extends InstagramGetRequest<InstagramGetUserFollowersResult> {

    private long userId;
    private String maxId;

    @Override
    public String getUrl() {
        String baseUrl = "friendships/" + userId + "/followers/?rank_token=" + api.getRankToken();
        if (StringUtils.isNotEmpty(maxId)) {
            baseUrl += "&max_id=" + maxId;
        }

        return baseUrl;
    }

    @Override
    @SneakyThrows
    public InstagramGetUserFollowersResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramGetUserFollowersResult.class);
    }

}
