package com.kiselev.enemy.network.instagram.api.internal.request;

import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramSearchUsernameResult;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class InstagramGetUserInfoRequest extends InstagramGetRequest<InstagramSearchUsernameResult> {

    private long userId;

    @Override
    public String getUrl() {
        return "users/" + userId + "/info/";
    }

    @Override
    @SneakyThrows
    public InstagramSearchUsernameResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramSearchUsernameResult.class);
    }

}
