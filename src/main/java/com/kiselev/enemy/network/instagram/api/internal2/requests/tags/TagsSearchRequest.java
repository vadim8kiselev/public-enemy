package com.kiselev.enemy.network.instagram.api.internal2.requests.tags;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.tags.TagsSearchResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
public class TagsSearchRequest extends IGGetRequest<TagsSearchResponse> {
    @NonNull
    private String query;
    private Double lat, lon;
    private String page_token, rank_token;
    private final int count = 30;

    @Override
    public String path() {
        return "tags/search";
    }

    @Override
    public String getQueryString(IGClient client) {

        return mapQueryString(
                "search_surface", "hashtag_search_page",
                "timezone_offset", "0",
                "q", query,
                "lat", lat,
                "lng", lon,
                "count", count,
                "rank_token", rank_token,
                "page_token", page_token);
    }

    @Override
    public Class<TagsSearchResponse> getResponseType() {
        return TagsSearchResponse.class;
    }

}
