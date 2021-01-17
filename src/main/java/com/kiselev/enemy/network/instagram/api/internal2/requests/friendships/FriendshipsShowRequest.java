package com.kiselev.enemy.network.instagram.api.internal2.requests.friendships;

import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.friendships.FriendshipsShowResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FriendshipsShowRequest extends IGGetRequest<FriendshipsShowResponse> {
    @NonNull
    private String pk;

    @Override
    public String path() {
        return "friendships/show/" + pk + "/";
    }

    @Override
    public Class<FriendshipsShowResponse> getResponseType() {
        return FriendshipsShowResponse.class;
    }

}
