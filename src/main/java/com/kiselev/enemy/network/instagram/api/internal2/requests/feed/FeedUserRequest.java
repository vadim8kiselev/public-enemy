package com.kiselev.enemy.network.instagram.api.internal2.requests.feed;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPaginatedRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.feed.FeedUserResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor
public class FeedUserRequest extends IGGetRequest<FeedUserResponse>
        implements IGPaginatedRequest<FeedUserResponse> {
    @NonNull
    private String pk;
    @Setter
    private String max_id;

    @Override
    public String path() {
        return "feed/user/" + pk + "/";
    }

    @Override
    public String getQueryString(IGClient client) {
        return this.mapQueryString("max_id", max_id);
    }

    @Override
    public Class<FeedUserResponse> getResponseType() {
        return FeedUserResponse.class;
    }

}
