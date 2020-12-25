package com.kiselev.enemy.network.instagram.api.internal2.requests.feed;

import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.feed.FeedReelsTrayResponse;

public class FeedReelsTrayRequest extends IGGetRequest<FeedReelsTrayResponse> {

    @Override
    public String path() {
        return "feed/reels_tray/";
    }

    @Override
    public Class<FeedReelsTrayResponse> getResponseType() {
        return FeedReelsTrayResponse.class;
    }

}
