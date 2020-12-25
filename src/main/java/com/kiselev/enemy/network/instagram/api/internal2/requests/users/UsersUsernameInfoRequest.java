package com.kiselev.enemy.network.instagram.api.internal2.requests.users;

import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.users.UserResponse;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsersUsernameInfoRequest extends IGGetRequest<UserResponse> {
    @NonNull
    private String username;

    @Override
    public String path() {
        return "users/" + username + "/usernameinfo/";
    }

    @Override
    public Class<UserResponse> getResponseType() {
        return UserResponse.class;
    }

}
