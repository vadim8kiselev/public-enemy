package com.kiselev.enemy.network.instagram.api.internal2.requests.feed;

import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.feed.FeedUserReelsMediaResponse;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FeedUserReelMediaRequest extends IGGetRequest<FeedUserReelsMediaResponse> {
    @NonNull
    private Long pk;

    @Override
    public String path() {
        return "feed/user/" + pk.toString() + "/reel_media/";
    }

    @Override
    public Class<FeedUserReelsMediaResponse> getResponseType() {
        return FeedUserReelsMediaResponse.class;
    }

}
