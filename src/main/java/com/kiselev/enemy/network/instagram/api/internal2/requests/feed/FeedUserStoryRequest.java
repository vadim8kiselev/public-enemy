package com.kiselev.enemy.network.instagram.api.internal2.requests.feed;

import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.feed.FeedUserStoryResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FeedUserStoryRequest extends IGGetRequest<FeedUserStoryResponse> {
    @NonNull
    private Long pk;

    @Override
    public String path() {
        return "feed/user/" + pk.toString() + "/story/";
    }

    @Override
    public Class<FeedUserStoryResponse> getResponseType() {
        return FeedUserStoryResponse.class;
    }

}
