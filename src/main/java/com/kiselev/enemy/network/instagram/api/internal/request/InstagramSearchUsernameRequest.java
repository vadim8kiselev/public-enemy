package com.kiselev.enemy.network.instagram.api.internal.request;

import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramSearchUsernameResult;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class InstagramSearchUsernameRequest extends InstagramGetRequest<InstagramSearchUsernameResult> {

    private String username;

    @Override
    public String getUrl() {
        return "users/" + username + "/usernameinfo/";
    }

    @Override
    @SneakyThrows
    public InstagramSearchUsernameResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramSearchUsernameResult.class);
    }

}
