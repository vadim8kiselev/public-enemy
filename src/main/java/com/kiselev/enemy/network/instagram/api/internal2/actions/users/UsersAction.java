package com.kiselev.enemy.network.instagram.api.internal2.actions.users;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.models.user.User;
import com.kiselev.enemy.network.instagram.api.internal2.requests.users.UsersInfoRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.users.UsersSearchRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.users.UsersUsernameInfoRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.users.UserResponse;
import com.kiselev.enemy.network.instagram.api.internal2.responses.users.UsersSearchResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class UsersAction {
    @NonNull
    private IGClient client;

    public CompletableFuture<com.kiselev.enemy.network.instagram.api.internal2.actions.users.UserAction> findByUsername(String username) {
        return new UsersUsernameInfoRequest(username).execute(client)
                .thenApply(response -> new UserAction(client, response.getUser()));
    }

    public CompletableFuture<User> info(long pk) {
        return new UsersInfoRequest(pk).execute(client)
                .thenApply(UserResponse::getUser);
    }

    public CompletableFuture<UsersSearchResponse> search(String query) {
        return new UsersSearchRequest(query).execute(client);
    }

}
