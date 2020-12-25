package com.kiselev.enemy.network.instagram.api.internal2.responses.friendships;

import java.util.Map;

import com.kiselev.enemy.network.instagram.api.internal2.models.friendships.Friendship;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;

import lombok.Data;

@Data
public class FriendshipsShowManyResponse extends IGResponse {
    private Map<String, Friendship> friendship_statuses;
}
