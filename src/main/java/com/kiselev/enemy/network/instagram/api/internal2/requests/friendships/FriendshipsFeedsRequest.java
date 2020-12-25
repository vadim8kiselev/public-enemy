package com.kiselev.enemy.network.instagram.api.internal2.requests.friendships;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPaginatedRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.feed.FeedUsersResponse;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@RequiredArgsConstructor
public class FriendshipsFeedsRequest extends IGGetRequest<FeedUsersResponse> implements IGPaginatedRequest<FeedUsersResponse> {
    @NonNull
    private Long _id;
    @NonNull
    private FriendshipsFeeds action;
    @Setter
    private String max_id;

    @Override
    public String getQueryString(IGClient client) {
        return mapQueryString("max_id", max_id);
    }

    @Override
    public String path() {
        return String.format("friendships/%s/%s/", _id, action.name().toLowerCase());
    }

    @Override
    public Class<FeedUsersResponse> getResponseType() {
        return FeedUsersResponse.class;
    }

    public static enum FriendshipsFeeds {
        FOLLOWERS, FOLLOWING;
    }
}
