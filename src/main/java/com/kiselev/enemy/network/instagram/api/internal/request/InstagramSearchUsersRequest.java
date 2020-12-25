package com.kiselev.enemy.network.instagram.api.internal.request;

import com.kiselev.enemy.network.instagram.api.internal.constants.InstagramConstants;
import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramSearchUsersResult;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class InstagramSearchUsersRequest extends InstagramGetRequest<InstagramSearchUsersResult> {

    private String query;

    @Override
    public String getUrl() {
        return "users/search/?ig_sig_key_version=" + InstagramConstants.API_KEY_VERSION
                + "&is_typeahead=true&query=" + query + "&rank_token=" + api.getRankToken();
    }

    @Override
    @SneakyThrows
    public InstagramSearchUsersResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramSearchUsersResult.class);
    }

}
