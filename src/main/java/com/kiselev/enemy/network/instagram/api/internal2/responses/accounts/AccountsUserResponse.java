package com.kiselev.enemy.network.instagram.api.internal2.responses.accounts;

import com.kiselev.enemy.network.instagram.api.internal2.models.user.User;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;

import lombok.Data;

@Data
public class AccountsUserResponse extends IGResponse {
    private User user;
}
