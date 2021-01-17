package com.kiselev.enemy.network.instagram.api.internal2.actions.users;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.actions.feed.FeedIterable;
import com.kiselev.enemy.network.instagram.api.internal2.models.friendships.Friendship;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.Profile;
import com.kiselev.enemy.network.instagram.api.internal2.requests.friendships.FriendshipsActionRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.friendships.FriendshipsActionRequest.FriendshipsAction;
import com.kiselev.enemy.network.instagram.api.internal2.requests.friendships.FriendshipsFeedsRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.friendships.FriendshipsFeedsRequest.FriendshipsFeeds;
import com.kiselev.enemy.network.instagram.api.internal2.requests.friendships.FriendshipsShowRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.feed.FeedUsersResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.friendships.FriendshipStatusResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.friendships.FriendshipsShowResponse;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class UserAction {
    @NonNull
    private IGClient client;
    @NonNull
    @Getter
    private Profile user;

    public FeedIterable<FriendshipsFeedsRequest, FeedUsersResponse> followersFeed() {
        return new FeedIterable<>(client, () ->
                new FriendshipsFeedsRequest(user.id(), FriendshipsFeeds.FOLLOWERS));
    }

    public FeedIterable<FriendshipsFeedsRequest, FeedUsersResponse> followingFeed() {
        return new FeedIterable<>(client, () ->
                new FriendshipsFeedsRequest(user.id(), FriendshipsFeeds.FOLLOWING));
    }

    public CompletableFuture<Friendship> getFriendship() {
        return new FriendshipsShowRequest(user.id()).execute(client)
                .thenApply(FriendshipsShowResponse::getFriendship);
    }

    public CompletableFuture<FriendshipStatusResponse> action(FriendshipsAction action) {
        return new FriendshipsActionRequest(user.id(), action).execute(client);
    }
}
