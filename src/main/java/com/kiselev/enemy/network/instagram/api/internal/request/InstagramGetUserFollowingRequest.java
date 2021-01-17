package com.kiselev.enemy.network.instagram.api.internal.request;

import com.kiselev.enemy.network.instagram.api.internal.constants.InstagramConstants;
import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramGetUserFollowersResult;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
@AllArgsConstructor
public class InstagramGetUserFollowingRequest extends InstagramGetRequest<InstagramGetUserFollowersResult> {

    private long userId;
    private String maxId;

    @Override
    public String getUrl() {
        String baseUrl = "friendships/" + userId + "/following/?rank_token=" + api.getRankToken() + "&ig_sig_key_version=" + InstagramConstants.API_KEY_VERSION;
        if (maxId != null && !maxId.isEmpty()) {
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
