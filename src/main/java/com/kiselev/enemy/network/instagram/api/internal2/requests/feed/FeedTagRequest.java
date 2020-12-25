package com.kiselev.enemy.network.instagram.api.internal2.requests.feed;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPaginatedRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.feed.FeedTagResponse;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor
public class FeedTagRequest extends IGGetRequest<FeedTagResponse>
        implements IGPaginatedRequest<FeedTagResponse> {
    @NonNull
    private String tag;
    @Setter
    private String max_id;

    @Override
    public String path() {
        return "feed/tag/" + tag + "/";
    }

    @Override
    public String getQueryString(IGClient client) {
        return mapQueryString("max_id", max_id);
    }

    @Override
    public Class<FeedTagResponse> getResponseType() {
        return FeedTagResponse.class;
    }

}
