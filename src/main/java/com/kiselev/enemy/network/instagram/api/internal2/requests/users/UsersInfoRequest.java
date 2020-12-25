package com.kiselev.enemy.network.instagram.api.internal2.requests.users;

import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.users.UserResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UsersInfoRequest extends IGGetRequest<UserResponse> {
    private long userId;

    @Override
    public String path() {
        return "users/" + userId + "/info/?from_module=blended_search";
    }

    @Override
    public Class<UserResponse> getResponseType() {
        return UserResponse.class;
    }

}
