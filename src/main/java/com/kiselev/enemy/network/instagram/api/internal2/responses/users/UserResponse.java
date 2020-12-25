package com.kiselev.enemy.network.instagram.api.internal2.responses.users;

import com.kiselev.enemy.network.instagram.api.internal2.models.user.User;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;

import lombok.Data;

@Data
public class UserResponse extends IGResponse {
    private User user;
}
