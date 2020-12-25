package com.kiselev.enemy.network.instagram.api.internal.request;

import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramFeedResult;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * Location Feed Request
 *
 * @author Yumaev
 */
@RequiredArgsConstructor
@AllArgsConstructor
public class InstagramLocationFeedRequest extends InstagramGetRequest<InstagramFeedResult> {
    @NonNull
    private final String location;
    private String maxId;

    @Override
    public String getUrl() {
        String url = "feed/location/" + location + "/?rank_token=" + api.getRankToken() + "&ranked_content=true&";
        if (maxId != null && !maxId.isEmpty()) {
            url += "max_id=" + maxId;
        }
        return url;
    }

    @Override
    @SneakyThrows
    public InstagramFeedResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramFeedResult.class);
    }
}
