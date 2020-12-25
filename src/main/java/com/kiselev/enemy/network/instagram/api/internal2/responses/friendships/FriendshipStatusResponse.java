package com.kiselev.enemy.network.instagram.api.internal2.responses.friendships;

import com.kiselev.enemy.network.instagram.api.internal2.models.friendships.Friendship;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;

import lombok.Data;

@Data
public class FriendshipStatusResponse extends IGResponse {
    private Friendship friendship_status;
}
