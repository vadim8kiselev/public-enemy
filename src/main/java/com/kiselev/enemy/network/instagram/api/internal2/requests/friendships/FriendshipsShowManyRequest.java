package com.kiselev.enemy.network.instagram.api.internal2.requests.friendships;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGPayload;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPostRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.friendships.FriendshipsShowManyResponse;
import lombok.Getter;
import lombok.NonNull;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FriendshipsShowManyRequest extends IGPostRequest<FriendshipsShowManyResponse> {
    @NonNull
    private String _user_ids;

    public FriendshipsShowManyRequest(Long... user_pks) {
        this._user_ids = Stream.of(user_pks).map(Object::toString).collect(Collectors.joining(","));
    }

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IGPayload() {
            @Getter
            private String user_ids = _user_ids;
        };
    }

    @Override
    public String path() {
        return "friendships/show_many/";
    }

    @Override
    public Class<FriendshipsShowManyResponse> getResponseType() {
        return FriendshipsShowManyResponse.class;
    }
}
