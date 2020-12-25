package com.kiselev.enemy.network.instagram.api.internal2.requests.feed;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPaginatedRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.feed.FeedLocationResponse;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor
public class FeedLocationRequest extends IGGetRequest<FeedLocationResponse>
        implements IGPaginatedRequest<FeedLocationResponse> {
    @NonNull
    private Long location;
    @Setter
    private String max_id;

    @Override
    public String path() {
        return "feed/location/" + location + "/";
    }

    @Override
    public String getQueryString(IGClient client) {
        return mapQueryString("max_id", max_id);
    }

    @Override
    public Class<FeedLocationResponse> getResponseType() {
        return FeedLocationResponse.class;
    }

}
