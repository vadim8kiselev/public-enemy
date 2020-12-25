package com.kiselev.enemy.network.instagram.api.internal2.responses.friendships;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kiselev.enemy.network.instagram.api.internal2.models.friendships.Friendship;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;

import lombok.Data;

@Data
public class FriendshipsShowResponse extends IGResponse {
    @JsonUnwrapped
    private Friendship friendship;
}
